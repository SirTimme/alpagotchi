package bot.commands.member;

import bot.commands.ISlashCommand;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.Env;
import bot.utils.MessageService;
import bot.utils.Responses;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.Locale;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Image implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Image.class);

    @Override
    public void execute(final SlashCommandEvent event, final Locale locale, final Entry user) {
        final String query = event.getOption("query").getAsString();
        if (query.length() > 100) {
            MessageService.queueReply(event, new MessageFormat(Responses.get("queryTooLong", locale)), true);
            return;
        }

        final JsonObject response = getImages(query);
        if (response == null) {
            MessageService.queueReply(event, new MessageFormat(Responses.get("apiError", locale)), true);
            return;
        }

        final JsonArray images = response.getAsJsonArray("hits");
        if (images.isEmpty()) {
            final MessageFormat msg = new MessageFormat(Responses.get("noSearchResults", locale));
            final String content = msg.format(new Object[]{ query });

            MessageService.queueReply(event, content, true);
            return;
        }

        final JsonObject randomImg = images.get((int) (Math.random() * images.size())).getAsJsonObject();
        final String imageUrl = randomImg.get("largeImageURL").getAsString();

        event.getJDA().retrieveUserById(Env.get("DEV_ID")).queue(dev -> {
            final MessageEmbed embed = new EmbedBuilder()
                    .setTitle("Result")
                    .setDescription("_**" + response.get("totalHits").getAsString() + "** Hits for " + query + "._")
                    .addField("Views", randomImg.get("views").getAsString(), true)
                    .addField("Likes", randomImg.get("likes").getAsString(), true)
                    .addField("Source", "[Direct Link](" + imageUrl + ")", true)
                    .setImage(imageUrl)
                    .setThumbnail("https://cdn.discordapp.com/attachments/840135073835122699/842742541148880896/internet.png")
                    .setFooter("Created by" + dev.getName(), dev.getAvatarUrl())
                    .setTimestamp(Instant.now())
                    .build();

            MessageService.queueEmbedReply(event, embed, false);
        });
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("image", "Search pixabay with your query")
                .addOptions(
                        new OptionData(STRING, "query", "The query you want to search", true)
                );
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.INFO;
    }

    private JsonObject getImages(final String query) {
        try {
            final HttpRequest request = HttpRequest.newBuilder().uri(buildURI(query)).GET().build();
            final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            final HttpResponse<String> response = HttpClient.newHttpClient().send(request, handler);

            return new Gson().fromJson(response.body(), JsonObject.class);
        }
        catch (final URISyntaxException | IOException | InterruptedException error) {
            LOGGER.error(error.getMessage());
        }

        return null;
    }

    private URI buildURI(final String query) throws URISyntaxException {
        return new URIBuilder("https://pixabay.com/api/")
                .addParameter("key", Env.get("API_KEY"))
                .addParameter("q", query)
                .addParameter("safesearch", "true")
                .addParameter("lang", "en")
                .addParameter("orientation", "horizontal")
                .build();
    }
}
