package bot.commands.member;

import bot.commands.IInfoCommand;
import bot.utils.Env;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;

import static bot.utils.Emote.REDCROSS;
import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Image implements IInfoCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Image.class);

    @Override
    public void execute(SlashCommandEvent event) {
        final String query = event.getOption("query").getAsString();
        if (query.length() > 100) {
            event.reply(REDCROSS + " The query must not exceed **100** characters").setEphemeral(true).queue();
            return;
        }

        final JsonObject response = getImagesFromAPI(query);
        if (response == null) {
            event.reply(REDCROSS + " There was an error while computing the API request")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final JsonArray images = response.getAsJsonArray("hits");
        if (images.isEmpty()) {
            event.reply(REDCROSS + " No results found for **" + query + "**")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final JsonObject randomImg = images.get((int) (Math.random() * images.size())).getAsJsonObject();
        final String imageURL = randomImg.get("largeImageURL").getAsString();

        final User dev = event.getJDA().getUserById(Env.get("DEV_ID"));

        final EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Result")
                .setDescription("_**" + response.get("totalHits").getAsString() + "** Hits for " + query + "._")
                .addField("Views", randomImg.get("views").getAsString(), true)
                .addField("Likes", randomImg.get("likes").getAsString(), true)
                .addField("Source", "[Direct Link](" + imageURL + ")", true)
                .setImage(imageURL)
                .setThumbnail("https://cdn.discordapp.com/attachments/840135073835122699/842742541148880896/internet.png")
                .setFooter("Created by" + dev.getName(), dev.getAvatarUrl())
                .setTimestamp(Instant.now());

        event.replyEmbeds(embed.build()).queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("image", "Search pixabay with your query")
                .addOptions(
                        new OptionData(STRING, "query", "The query you want to search", true)
                );
    }

    private JsonObject getImagesFromAPI(String query) {
        final HttpResponse<String> response;
        try {
            final URIBuilder requestURI = new URIBuilder("https://pixabay.com/api/")
                    .addParameter("key", Env.get("API_KEY"))
                    .addParameter("q", query)
                    .addParameter("safesearch", "true")
                    .addParameter("lang", "en")
                    .addParameter("orientation", "horizontal");
            final HttpRequest request = HttpRequest.newBuilder().uri(requestURI.build()).GET().build();
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException error) {
            LOGGER.error(error.getMessage());
            return null;
        }
        return new Gson().fromJson(response.body(), JsonObject.class);
    }
}
