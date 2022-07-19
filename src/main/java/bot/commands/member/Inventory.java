package bot.commands.member;

import bot.commands.UserCommand;
import bot.models.Entry;
import bot.shop.ItemManager;
import bot.utils.CommandType;
import bot.utils.Env;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.time.Instant;
import java.util.Locale;

public class Inventory extends UserCommand {
    private final ItemManager items;

    public Inventory(final ItemManager items) {
        this.items = items;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final Entry user) {
        event.getJDA().retrieveUserById(Env.get("DEV_ID")).queue(dev -> {
            final var embed = new EmbedBuilder()
                    .setTitle("Inventory")
                    .setThumbnail("https://cdn.discordapp.com/attachments/795637300661977132/839074173459365908/inventory.png")
                    .addField("__**:meat_on_bone: Hunger items**__", "These items are used to fill up the hunger of your alpaca", false)
                    .setFooter("Created by " + dev.getName(), dev.getAvatarUrl())
                    .setTimestamp(Instant.now());

            this.items.getItemsByStat("hunger").forEach(item -> embed.addField(
                    ":package: " + item.getName(),
                    "Quantity: **" + user.getItem(item.getName()) + "**",
                    true)
            );

            embed.addBlankField(false).addField(
                    "__**:beer: Thirst items**__",
                    "Following items replenish the thirst of your alpaca",
                    false
            );

            this.items.getItemsByStat("thirst").forEach(item -> embed.addField(
                    ":package: " + item.getName(),
                    "Quantity: **" + user.getItem(item.getName()) + "**",
                    true)
            );

            event.replyEmbeds(embed.build()).queue();
        });
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("inventory", "Shows your items")
                       .setDescriptionLocalization(DiscordLocale.GERMAN, "Zeigt deine Items");
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.INFO;
    }
}