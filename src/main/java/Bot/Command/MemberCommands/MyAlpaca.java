package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.PermissionLevel;
import Bot.Config;
import Bot.Database.IDataBaseManager;
import Bot.Utils.ImagePreloader;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.PermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MyAlpaca implements ICommand {
	private static final Logger LOGGER = LoggerFactory.getLogger(MyAlpaca.class);

	@Override
	public void execute(CommandContext ctx) throws PermissionException {
		if (!IDataBaseManager.INSTANCE.isUserInDB(ctx.getAuthorID())) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> You do not own a alpaca, use **" + ctx.getPrefix() + "init** first").queue();
			return;
		}

		if (!ctx.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
			throw new PermissionException("Cannot perform action due to a lack of Permission. Missing permission: " + Permission.MESSAGE_EMBED_LINKS);
		}

		final int hunger = IDataBaseManager.INSTANCE.getAlpacaValues(ctx.getAuthorID(), "hunger");
		final int thirst = IDataBaseManager.INSTANCE.getAlpacaValues(ctx.getAuthorID(), "thirst");
		final int energy = IDataBaseManager.INSTANCE.getAlpacaValues(ctx.getAuthorID(), "energy");
		final int joy = IDataBaseManager.INSTANCE.getAlpacaValues(ctx.getAuthorID(), "joy");

		final String outfitName = IDataBaseManager.INSTANCE.getOutfit(ctx.getAuthorID());
		final BufferedImage background = ImagePreloader.getAlpacaImage(outfitName);
		final BufferedImage img = new BufferedImage(background.getWidth(), background.getHeight(), BufferedImage.TYPE_INT_RGB);

		final Graphics graphics = img.createGraphics();
		graphics.setFont(new Font("SansSerif", Font.BOLD, 15));

		graphics.drawImage(background, 0, 0, null);

		graphics.setColor(Color.BLACK);
		graphics.drawString(hunger + "/100", getPosition(hunger, "front"), 24);
		graphics.drawString(thirst + "/100", getPosition(thirst, "front"), 66);
		graphics.drawString(energy + "/100", getPosition(energy, "back"), 24);
		graphics.drawString(joy + "/100", getPosition(joy, "back"), 66);

		graphics.setColor(getColorOfValues(hunger));
		graphics.fillRect(31, 31, (int) (hunger * 1.75), 12);

		graphics.setColor(getColorOfValues(thirst));
		graphics.fillRect(31, 73, (int) (thirst * 1.75), 12);

		graphics.setColor(getColorOfValues(energy));
		graphics.fillRect(420, 31, (int) (energy * 1.75), 12);

		graphics.setColor(getColorOfValues(joy));
		graphics.fillRect(420, 73, (int) (joy * 1.75), 12);

		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			ImageIO.write(img, "jpg", byteStream);

			long sleepCooldown = IDataBaseManager.INSTANCE.getCooldown(ctx.getAuthorID(), "sleep") - System.currentTimeMillis();
			String sleepMsg = sleepCooldown > 0 ? "<:RedCross:782229279312314368> " + (int) TimeUnit.MILLISECONDS.toMinutes(sleepCooldown) + " minutes" : "<:GreenTick:782229268914372609> ready";

			long workCooldown = IDataBaseManager.INSTANCE.getCooldown(ctx.getAuthorID(), "work") - System.currentTimeMillis();
			String workMsg = workCooldown > 0 ? "<:RedCross:782229279312314368> " + (int) TimeUnit.MILLISECONDS.toMinutes(workCooldown) + " minutes" : "<:GreenTick:782229268914372609> ready";

			final User botCreator = ctx.getJDA().getUserById(Config.get("OWNER_ID"));
			final EmbedBuilder embed = new EmbedBuilder();
			embed
					.setTitle("" + IDataBaseManager.INSTANCE.getNickname(ctx.getAuthorID()) + "")
					.setDescription("_Have a llamazing day!_")
					.addField("Work", workMsg, true)
					.addField("Sleep", sleepMsg, true)
					.setThumbnail(ctx.getMember().getUser().getAvatarUrl())
					.setFooter("Created by " + botCreator.getName(), botCreator.getEffectiveAvatarUrl())
					.setTimestamp(Instant.now())
					.setImage("attachment://alpagotchi.jpg");

			ctx.getChannel().sendFile(byteStream.toByteArray(), "alpagotchi.jpg").embed(embed.build()).queue();
		} catch (IOException error) {
			LOGGER.error(error.getMessage());
		}
	}

	@Override
	public String getHelp(String prefix) {
		return "`Usage: " + prefix + "myalpaca\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Shows the stats of your alpaca";
	}

	@Override
	public String getName() {
		return "myalpaca";
	}

	@Override
	public Enum<PermissionLevel> getPermissionLevel() {
		return PermissionLevel.MEMBER;
	}

	@Override
	public List<String> getAliases() {
		return List.of("ma", "stats");
	}

	private Color getColorOfValues(int value) {
		if (value >= 80) {
			return Color.GREEN;
		} else if (value >= 60) {
			return Color.YELLOW;
		} else if (value >= 40) {
			return Color.ORANGE;
		} else if (value >= 20) {
			return Color.RED;
		} else {
			return Color.BLACK;
		}
	}

	private int getPosition(int value, String position) {
		if (value == 100) {
			return position.equalsIgnoreCase("front") ? 145 : 534;
		} else if (value >= 10) {
			return position.equalsIgnoreCase("front") ? 155 : 544;
		} else {
			return position.equalsIgnoreCase("front") ? 165 : 554;
		}
	}
}
