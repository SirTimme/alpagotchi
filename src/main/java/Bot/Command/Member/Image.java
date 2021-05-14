package Bot.Command.Member;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Config;
import Bot.Utils.Emote;
import Bot.Utils.Error;
import Bot.Utils.Level;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
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
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;

public class Image implements ICommand {
	private static final Logger LOGGER = LoggerFactory.getLogger(Image.class);

	@Override
	public void execute(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final String query = String.join(" ", ctx.getArgs());

		if (query.isEmpty()) {
			channel.sendMessage(Error.MISSING_ARGS.getMessage(ctx.getPrefix(), getName())).queue();
			return;
		}

		if (query.length() > 100) {
			channel.sendMessage(Emote.REDCROSS + " The query must not exceed **100** characters").queue();
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
			final HttpResponse<String> response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());

			final JSONObject json = new JSONObject(response.body());
			final JSONArray hits = json.getJSONArray("hits");

			if (hits.isEmpty()) {
				channel.sendMessage(Emote.REDCROSS + " No results found for **" + query + "**").queue();
				return;
			}

			final JSONObject object = hits.getJSONObject((int) (Math.random() * hits.length()));
			final String imageURL = object.getString("largeImageURL");

			final User dev = ctx.getJDA().getUserById(Config.get("DEV_ID"));
			final EmbedBuilder embed = new EmbedBuilder();

			embed.setTitle("Result")
				 .setDescription("_**" + json.getInt("totalHits") + "** Hits for " + query + ".\nUses pixabay for searching images._")
				 .addField("Views", String.valueOf(object.getInt("views")), true)
				 .addField("Likes", String.valueOf(object.getInt("likes")), true)
				 .addField("Source", "[Direct Link](" + imageURL + ")", true)
				 .setImage(imageURL)
				 .setThumbnail("https://cdn.discordapp.com/attachments/840135073835122699/842742541148880896/internet.png")
				 .setFooter("Created by " + dev.getName(), dev.getEffectiveAvatarUrl())
				 .setTimestamp(Instant.now());

			channel.sendMessage(embed.build()).queue();
		}
		catch (URISyntaxException | InterruptedException | IOException error) {
			LOGGER.error(error.getMessage());
		}
	}

	@Override
	public String getName() {
		return "image";
	}

	@Override
	public Level getLevel() {
		return Level.MEMBER;
	}

	@Override
	public List<String> getAliases() {
		return List.of("img");
	}

	@Override
	public EnumSet<Permission> getCommandPerms() {
		return EnumSet.of(Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_WRITE);
	}

	@Override
	public String getSyntax() {
		return "image [query]";
	}

	@Override
	public String getExample() {
		return "image alpaca";
	}

	@Override
	public String getDescription() {
		return "Uses the pixabay search engine to find a random image of the specified word.\nNSFW-Posts gets filtered";
	}
}
