package bot.commands.member;

import bot.commands.UserCommand;
import bot.models.Entry;
import bot.utils.Env;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MyAlpaca extends UserCommand {
	private final Map<String, BufferedImage> images = new HashMap<>();
	private static final Logger LOGGER = LoggerFactory.getLogger(MyAlpaca.class);
	private final Color[] colors = { Color.BLACK, Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN };

	public MyAlpaca() {
		final File folder = new File("src/main/resources/outfits");
		try {
			for (File file : folder.listFiles()) {
				final String name = file.getName().split("\\.")[0];

				this.images.put(name, ImageIO.read(file));
			}
		} catch (IOException error) {
			LOGGER.error(error.getMessage());
		}
	}

	@Override
	public void execute(final SlashCommandEvent event, final Locale locale, final Entry user) {
		final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		try {
			ImageIO.write(createImage(user), "jpg", bytes);
		} catch (IOException error) {
			LOGGER.error(error.getMessage());
		}

		final User dev = event.getJDA().getUserById(Env.get("DEV_ID"));

		final MessageEmbed embed = new EmbedBuilder()
				.setTitle(user.getNickname())
				.setDescription("_Have a llamazing day!_")
				.addField("Work", getCooldownMsg(user.getWorkAsMinutes(), locale), true)
				.addField("Sleep", getCooldownMsg(user.getSleepAsMinutes(), locale), true)
				.setThumbnail(event.getUser().getAvatarUrl())
				.setFooter("Created by " + dev.getName(), dev.getAvatarUrl())
				.setTimestamp(Instant.now())
				.setImage("attachment://alpagotchi.jpg")
				.build();

		MessageService.queueReply(event, embed, bytes.toByteArray(),false);
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData("myalpaca", "Shows your alpaca with its stats");
	}

	private BufferedImage createImage(Entry user) {
		final int hunger = user.getHunger();
		final int thirst = user.getThirst();
		final int energy = user.getEnergy();
		final int joy = user.getJoy();

		final BufferedImage background = this.images.get(user.getOutfit());
		final BufferedImage img = new BufferedImage(background.getWidth(), background.getHeight(), BufferedImage.TYPE_INT_RGB);

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

		return img;
	}

	private Color getValueColor(int value) {
		return value == 100 ? Color.GREEN : this.colors[value / 20];
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

	private String getCooldownMsg(final long minutes, final Locale locale) {
		return minutes > 0 ? Responses.get("activeCooldown", locale) : Responses.get("inactiveCooldown", locale);
	}
}
