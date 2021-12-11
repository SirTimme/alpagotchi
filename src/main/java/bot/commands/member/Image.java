package bot.commands.member;

import bot.commands.interfaces.IInfoCommand;
import bot.utils.Env;
import bot.utils.MessageService;
import bot.utils.Responses;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
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
import java.text.MessageFormat;
import java.time.Instant;
import java.util.Locale;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Image implements IInfoCommand {
	private static final Logger LOGGER = LoggerFactory.getLogger(Image.class);

	@Override
	public void execute(final SlashCommandEvent event, final Locale locale) {
		final String query = event.getOption("query").getAsString();
		if (query.length() > 100) {
			MessageService.reply(event, new MessageFormat(Responses.get("queryTooLong", locale)), true);
			return;
		}
		// return response as result!
		final JsonObject response = getImagesFromAPI(query);
		if (response == null) {
			MessageService.reply(event, new MessageFormat(Responses.get("apiError", locale)), true);
			return;
		}

		final JsonArray images = response.getAsJsonArray("hits");
		if (images.isEmpty()) {
			final MessageFormat msg = new MessageFormat(Responses.get("noSearchResults", locale));
			MessageService.reply(event, msg.format(new Object[]{ query }), true);
			return;
		}

		final JsonObject randomImg = images.get((int) (Math.random() * images.size())).getAsJsonObject();
		final String imageURL = randomImg.get("largeImageURL").getAsString();

		final User dev = event.getJDA().getUserById(Env.get("DEV_ID"));
		final MessageEmbed embed = new EmbedBuilder()
				.setTitle("Result")
				.setDescription("_**" + response.get("totalHits").getAsString() + "** Hits for " + query + "._")
				.addField("Views", randomImg.get("views").getAsString(), true)
				.addField("Likes", randomImg.get("likes").getAsString(), true)
				.addField("Source", "[Direct Link](" + imageURL + ")", true)
				.setImage(imageURL)
				.setThumbnail("https://cdn.discordapp.com/attachments/840135073835122699/842742541148880896/internet.png")
				.setFooter("Created by" + dev.getName(), dev.getAvatarUrl())
				.setTimestamp(Instant.now())
				.build();

		event.replyEmbeds(embed).queue();
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
