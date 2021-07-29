package Bot.Command.Member;

import Bot.Command.ISlashCommand;
import Bot.Config;
import Bot.Utils.Emote;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Image implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Image.class);

    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        final String query = event.getOption("query").getAsString();

        if (query.length() > 100) {
            event.reply(Emote.REDCROSS + " The query must not exceed **100** characters").setEphemeral(true).queue();
            return;
        }

        try {
            final URIBuilder requestURI = new URIBuilder("https://pixabay.com/api/");
            requestURI.addParameter("key", Config.get("API_KEY"));
            requestURI.addParameter("q", query);
            requestURI.addParameter("safesearch", "true");
            requestURI.addParameter("lang", "en");
            requestURI.addParameter("orientation", "horizontal");

            final HttpRequest request = HttpRequest.newBuilder().uri(requestURI.build()).GET().build();
            final HttpResponse<String> response = HttpClient.newHttpClient()
                                                            .send(request, HttpResponse.BodyHandlers.ofString());

            final JSONObject json = new JSONObject(response.body());
            final JSONArray hits = json.getJSONArray("hits");

            if (hits.isEmpty()) {
                event.reply(Emote.REDCROSS + " No results found for **" + query + "**")
                     .setEphemeral(true)
                     .queue();
                return;
            }

            final JSONObject object = hits.getJSONObject((int) (Math.random() * hits.length()));
            final String imageURL = object.getString("largeImageURL");

            final EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Result")
                 .setDescription("_**" + json.getInt("totalHits") + "** Hits for " + query + "._")
                 .addField("Views", String.valueOf(object.getInt("views")), true)
                 .addField("Likes", String.valueOf(object.getInt("likes")), true)
                 .addField("Source", "[Direct Link](" + imageURL + ")", true)
                 .setImage(imageURL)
                 .setThumbnail("https://cdn.discordapp.com/attachments/840135073835122699/842742541148880896/internet.png")
                 .setFooter("Created by SirTimme", "https://cdn.discordapp.com/avatars/483012399893577729/ba3996b7728a950565a79bd4b550b8dd.png")
                 .setTimestamp(Instant.now());

            event.replyEmbeds(embed.build()).queue();
        } catch (URISyntaxException | IOException | InterruptedException error) {
            LOGGER.error(error.getMessage());
        }
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("image", "Search pixabay with your query")
                .addOptions(
                        new OptionData(STRING, "query", "The query you want to search", true)
                );
    }
}
