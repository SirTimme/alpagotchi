package bot.commands.member;

import bot.commands.types.UserCommand;
import bot.models.cooldown.CooldownUtils;
import bot.models.user.User;
import bot.commands.types.CommandType;
import bot.localization.LocalizedResponse;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Alpaca extends UserCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Alpaca.class);
    private final Map<String, BufferedImage> images = new HashMap<>();
    private final Color[] colors = { Color.BLACK, Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN };

    public Alpaca() {
        try {
            this.images.put("default", ImageIO.read(getClass().getResourceAsStream("/outfits/default.png")));
            this.images.put("gentleman", ImageIO.read(getClass().getResourceAsStream("/outfits/gentleman.png")));
            this.images.put("lady", ImageIO.read(getClass().getResourceAsStream("/outfits/lady.png")));
        } catch (final IOException error) {
            LOGGER.error(error.getMessage());
        }
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final User user) {
        final var bytes = new ByteArrayOutputStream();
        try {
            ImageIO.write(createImage(user), "png", bytes);
        } catch (final IOException error) {
            LOGGER.error(error.getMessage());
        }

        final var workCooldown = CooldownUtils.toMinutes(user.getCooldown().getWork());
        final var sleepCooldown = CooldownUtils.toMinutes(user.getCooldown().getSleep());
        final var nickname = user.getAlpaca().getNickname();

        final var embed = new EmbedBuilder()
                .setTitle(nickname)
                .setDescription(LocalizedResponse.get("alpaca.embed.description", locale))
                .addField(LocalizedResponse.get("alpaca.embed.field.title.work", locale), getCooldownMsg(workCooldown, locale), true)
                .addField(LocalizedResponse.get("alpaca.embed.field.title.sleep", locale), getCooldownMsg(sleepCooldown, locale), true)
                .setThumbnail(event.getUser().getAvatarUrl())
                .setFooter(LocalizedResponse.get("general.embed.footNote.createdBy", locale), "attachment://author.png")
                .setTimestamp(Instant.now())
                .setImage("attachment://alpagotchi.png")
                .build();

        event.replyEmbeds(embed)
             .addFiles(FileUpload.fromData(bytes.toByteArray(), "alpagotchi.png"))
             .queue();
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("alpaca", "Shows your alpaca")
                       .setDescriptionLocalization(DiscordLocale.GERMAN, "Zeigt dein Alpaka");
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.USER;
    }

    private BufferedImage createImage(final User user) {
        final int hunger = user.getAlpaca().getHunger();
        final int thirst = user.getAlpaca().getThirst();
        final int energy = user.getAlpaca().getEnergy();
        final int joy = user.getAlpaca().getJoy();

        final BufferedImage background = this.images.get(user.getAlpaca().getOutfit());
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
        return minutes > 0
                ? LocalizedResponse.get("alpaca.cooldown.active", locale, minutes)
                : LocalizedResponse.get("alpaca.cooldown.inactive", locale);
    }
}