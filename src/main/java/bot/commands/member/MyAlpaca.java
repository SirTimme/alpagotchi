package bot.commands.member;

import bot.commands.IUserCommand;
import bot.models.DBUser;
import bot.utils.Env;
import bot.utils.Language;
import net.dv8tion.jda.api.EmbedBuilder;
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
import java.util.Map;

import static bot.utils.Emote.GREENTICK;
import static bot.utils.Emote.REDCROSS;

public class MyAlpaca implements IUserCommand {
    private final Map<String, BufferedImage> images = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(MyAlpaca.class);
    private final Color[] colors = {Color.BLACK, Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN};

    public MyAlpaca() {
        final File folder = new File("src/main/resources/outfits");
        try {
            for (File file : folder.listFiles()) {
                final BufferedImage img = ImageIO.read(file);
                final String name = file.getName().split("\\.")[0];

                images.put(name, img);
            }
        } catch (IOException error) {
            LOGGER.error(error.getMessage());
        }
    }

    @Override
    public void execute(SlashCommandEvent event, DBUser user) {
        try {
            final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ImageIO.write(createImage(user), "png", bytes);

            final User dev = event.getJDA().getUserById(Env.get("DEV_ID"));

            final EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(user.getAlpaca().getNickname())
                 .setDescription("_Have a llamazing day!_")
                 .addField("Work", checkCooldown(user.getCooldown().getWork()), true)
                 .addField("Sleep", checkCooldown(user.getCooldown().getSleep()), true)
                 .setThumbnail(event.getUser().getAvatarUrl())
                 .setFooter("Created by " + dev.getName(), dev.getAvatarUrl())
                 .setTimestamp(Instant.now())
                 .setImage("attachment://alpagotchi.png");

            event.replyEmbeds(embed.build())
                 .addFile(bytes.toByteArray(), "alpagotchi.png")
                 .queue();
        } catch (IOException error) {
            LOGGER.error(error.getMessage());
        }
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("myalpaca", "Shows your alpaca with its stats");
    }

    private BufferedImage createImage(DBUser user) {
        final int hunger = user.getAlpaca().getHunger();
        final int thirst = user.getAlpaca().getThirst();
        final int energy = user.getAlpaca().getEnergy();
        final int joy = user.getAlpaca().getJoy();

        final BufferedImage background = images.get(user.getAlpaca().getOutfit());
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
        return value == 100 ? Color.GREEN : colors[value / 20];
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

    private String checkCooldown(long cooldown) {
        if (cooldown > 0) {
            return REDCROSS + " " + cooldown + " " + Language.handle(cooldown, "minute", "minutes");
        } else {
            return GREENTICK + " ready";
        }
    }
}
