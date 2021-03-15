package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.PermissionLevel;
import Bot.Config;
import Bot.Database.IDatabase;
import Bot.Utils.ResourcesManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MyAlpaca implements ICommand {
	private static final Logger LOGGER = LoggerFactory.getLogger(MyAlpaca.class);

	@Override
	public void execute(CommandContext ctx) {
		final long authorID = ctx.getAuthorID();
		final TextChannel channel = ctx.getChannel();

		if (!IDatabase.INSTANCE.isUserInDB(authorID)) {
			channel.sendMessage("<:RedCross:782229279312314368> You don't own an alpaca, " +
				"use **" + ctx.getPrefix() + "init** first")
				   .queue();
			return;
		}

		final int hunger = IDatabase.INSTANCE.getAlpacaValues(authorID, "hunger");
		final int thirst = IDatabase.INSTANCE.getAlpacaValues(authorID, "thirst");
		final int energy = IDatabase.INSTANCE.getAlpacaValues(authorID, "energy");
		final int joy = IDatabase.INSTANCE.getAlpacaValues(authorID, "joy");
		final String outfit = IDatabase.INSTANCE.getOutfit(authorID);

		final BufferedImage background = ResourcesManager.getAlpacaImage(outfit);
		final BufferedImage img = new BufferedImage(
			background.getWidth(),
			background.getHeight(),
			BufferedImage.TYPE_INT_RGB
		);

		final Graphics graphics = img.createGraphics();
		graphics.setFont(new Font("SansSerif", Font.BOLD, 15));

		graphics.drawImage(background, 0, 0, null);

		graphics.setColor(Color.BLACK);
		graphics.drawString(hunger + "/100", getPosition(hunger, "front"), 24);
		graphics.drawString(thirst + "/100", getPosition(thirst, "front"), 66);
		graphics.drawString(energy + "/100", getPosition(energy, "back"), 24);
		graphics.drawString(joy + "/100", getPosition(joy, "back"), 66);

		graphics.setColor(getValueColor(hunger));
		graphics.fillRect(31, 31, (int) (hunger * 1.75), 12);

		graphics.setColor(getValueColor(thirst));
		graphics.fillRect(31, 73, (int) (thirst * 1.75), 12);

		graphics.setColor(getValueColor(energy));
		graphics.fillRect(420, 31, (int) (energy * 1.75), 12);

		graphics.setColor(getValueColor(joy));
		graphics.fillRect(420, 73, (int) (joy * 1.75), 12);

		try {
			final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			ImageIO.write(img, "jpg", byteStream);

			final long sleepCooldown = IDatabase.INSTANCE.getCooldown(authorID, "sleep") - System.currentTimeMillis();
			final int sleepMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(sleepCooldown);
			final String sleepMsg = sleepCooldown > 0
									? "<:RedCross:782229279312314368> " + sleepMinutes + " minutes"
									: "<:GreenTick:782229268914372609> ready";

			final long workCooldown = IDatabase.INSTANCE.getCooldown(authorID, "work") - System.currentTimeMillis();
			final int workMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(workCooldown);
			final String workMsg = workCooldown > 0
								   ? "<:RedCross:782229279312314368> " + workMinutes + " minutes"
								   : "<:GreenTick:782229268914372609> ready";

			final User botCreator = ctx.getJDA().getUserById(Config.get("DEV_ID"));
			final EmbedBuilder embed = new EmbedBuilder();

			embed.setTitle(IDatabase.INSTANCE.getNickname(authorID))
				 .setDescription("_Have a llamazing day!_")
				 .addField("Work", workMsg, true)
				 .addField("Sleep", sleepMsg, true)
				 .setThumbnail(ctx.getMember().getUser().getAvatarUrl())
				 .setFooter("Created by " + botCreator.getName(), botCreator.getEffectiveAvatarUrl())
				 .setTimestamp(Instant.now())
				 .setImage("attachment://alpagotchi.jpg");

			channel.sendFile(byteStream.toByteArray(), "alpagotchi.jpg").embed(embed.build()).queue();
		}
		catch (IOException error) {
			LOGGER.error(error.getMessage());
		}
	}

	@Override
	public String getHelp(String prefix) {
		return "**Usage:** " + prefix + "myalpaca\n" +
			"**Aliases:** " + getAliases() + "\n" +
			"**Example**: " + prefix + "myalpaca";
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

	@Override
	public EnumSet<Permission> getRequiredPermissions() {
		return EnumSet.of(Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_ATTACH_FILES);
	}

	private Color getValueColor(int value) {
		if (value >= 80) {
			return Color.GREEN;
		}
		else if (value >= 60) {
			return Color.YELLOW;
		}
		else if (value >= 40) {
			return Color.ORANGE;
		}
		else if (value >= 20) {
			return Color.RED;
		}
		else {
			return Color.BLACK;
		}
	}

	private int getPosition(int value, String position) {
		if (value == 100) {
			return position.equalsIgnoreCase("front") ? 145 : 534;
		}
		else if (value >= 10) {
			return position.equalsIgnoreCase("front") ? 155 : 544;
		}
		else {
			return position.equalsIgnoreCase("front") ? 165 : 554;
		}
	}
}
