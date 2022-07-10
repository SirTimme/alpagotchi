package bot.commands.member;

import bot.commands.UserCommand;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.Env;
import bot.utils.Responses;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
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
            for (final var file : folder.listFiles()) {
                final var name = file.getName().split("\\.")[0];

                this.images.put(name, ImageIO.read(file));
            }
        }
        catch (final IOException error) {
            LOGGER.error(error.getMessage());
        }
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final Entry user) {
        final var bytes = new ByteArrayOutputStream();
        try {
            ImageIO.write(createImage(user), "png", bytes);
        }
        catch (final IOException error) {
            LOGGER.error(error.getMessage());
        }

        event.getJDA().retrieveUserById(Env.get("DEV_ID")).queue(dev -> {
            final var embed = new EmbedBuilder()
                    .setTitle(user.getNickname())
                    .setDescription("_Have a llamazing day!_")
                    .addField("Work", getCooldownMsg(user.getWork(), locale), true)
                    .addField("Sleep", getCooldownMsg(user.getSleep(), locale), true)
                    .setThumbnail(event.getUser().getAvatarUrl())
                    .setFooter("Created by " + dev.getName(), dev.getAvatarUrl())
                    .setTimestamp(Instant.now())
                    .setImage("attachment://alpagotchi.png")
                    .build();

            event.replyEmbeds(embed).addFile(bytes.toByteArray(), "alpagotchi.png").queue();
        });
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("myalpaca", "Shows your alpaca with its stats");
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.USER;
    }

    private BufferedImage createImage(final Entry user) {
        final int hunger = user.getHunger();
        final int thirst = user.getThirst();
        final int energy = user.getEnergy();
        final int joy = user.getJoy();

        final BufferedImage background = this.images.get(user.getOutfit());
        final BufferedImage img = new BufferedImage(background.getWidth(),
                                                    background.getHeight(),
                                                    BufferedImage.TYPE_INT_RGB);

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

    private Color getValueColor(final int value) {
        return value == 100 ? Color.GREEN : this.colors[value / 20];
    }

    private int getPosition(final int value, final String position) {
        if (value == 100) {
            return position.equalsIgnoreCase("front") ? 145 : 534;
        } else if (value >= 10) {
            return position.equalsIgnoreCase("front") ? 155 : 544;
        } else {
            return position.equalsIgnoreCase("front") ? 165 : 554;
        }
    }

    private String getCooldownMsg(final long minutes, final Locale locale) {
        if (minutes > 0) {
            final MessageFormat msg = new MessageFormat(Responses.get("activeCooldown", locale));
            return msg.format(new Object[]{ minutes });
        }
        return Responses.get("inactiveCooldown", locale);
    }
}