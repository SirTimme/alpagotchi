package Bot.Command.Member;

import Bot.Command.ISlashCommand;
import Bot.Models.Entry;
import Bot.Database.IDatabase;
import Bot.Utils.Emote;
import Bot.Utils.Language;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
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
import java.util.concurrent.TimeUnit;

public class MyAlpaca implements ISlashCommand {
    private final Map<String, BufferedImage> images = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(MyAlpaca.class);
    private final Color[] colors = {Color.BLACK, Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN};

    public MyAlpaca() {
        final File folder = new File("src/main/resources/outfits");
        try {
            for (File file : folder.listFiles()) {
                final BufferedImage image = ImageIO.read(file);
                final String key = file.getName().split("\\.")[0];

                this.images.put(key, image);
            }
        } catch (IOException error) {
            LOGGER.error(error.getMessage());
        }
    }

    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        Entry entry = IDatabase.INSTANCE.getEntry(authorID);

        if (entry == null) {
            event.reply(Emote.REDCROSS + " You don't own an alpaca, use **/init** first")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        try {
            final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

            ImageIO.write(createImage(entry), "jpg", byteStream);

            final long sleepCooldown = entry.getCooldown().getSleep() - System.currentTimeMillis();
            final long workCooldown = entry.getCooldown().getWork() - System.currentTimeMillis();

            final EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(entry.getAlpaca().getNickname())
                 .setDescription("_Have a llamazing day!_")
                 .addField("Work", checkCooldown(workCooldown), true)
                 .addField("Sleep", checkCooldown(sleepCooldown), true)
                 .setThumbnail(event.getUser().getAvatarUrl())
                 .setFooter("Created by SirTimme", "https://cdn.discordapp.com/avatars/483012399893577729/ba3996b7728a950565a79bd4b550b8dd.png")
                 .setTimestamp(Instant.now())
                 .setImage("attachment://alpagotchi.jpg");

            event.replyEmbeds(embed.build())
                 .addFile(byteStream.toByteArray(), "alpagotchi.jpg")
                 .queue();
        } catch (IOException error) {
            LOGGER.error(error.getMessage());
        }
    }

    private BufferedImage createImage(Entry entry) {
        final int hunger = entry.getAlpaca().getHunger();
        final int thirst = entry.getAlpaca().getThirst();
        final int energy = entry.getAlpaca().getEnergy();
        final int joy = entry.getAlpaca().getJoy();

        final BufferedImage background = images.get(entry.getAlpaca().getOutfit());
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
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(cooldown);
        return cooldown > 0 ? Emote.REDCROSS + " " + Language.handle(minutes, "minute") : Emote.GREENTICK + " ready";
    }
}
