package bot.commands.member;

import bot.commands.UserCommand;
import bot.models.Entry;
import bot.shop.Item;
import bot.shop.ItemManager;
import bot.utils.CommandType;
import bot.utils.Env;
import bot.utils.Responses;
import com.jakewharton.fliptables.FlipTable;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.time.Instant;
import java.util.Locale;

public class Inventory extends UserCommand {
    private final ItemManager itemManager;

    public Inventory(final ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final Entry user) {
        event.getJDA().retrieveUserById(Env.get("DEV_ID")).queue(dev -> {
            final var embed = new EmbedBuilder()
                    .setTitle("Inventory")
                    .setDescription("```ansi\nCurrent Balance: \u001B[1;34m" + user.getCurrency() + " Fluffies\n```\n```\n" + buildTable(user) + "\n```\n```ansi\nHow to buy: \u001B[1;34m/buy <item> <amount>\n```")
                    .setFooter(Responses.get("createdByNotice", locale), dev.getAvatarUrl())
                    .setTimestamp(Instant.now())
                    .build();

            event.replyEmbeds(embed).queue();
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

    private String buildTable(final Entry user) {
        final String[] header = { "Name", "Saturation", "Quantity" };
        final var content = this.itemManager.getItems()
                                            .stream()
                                            .map(item -> buildRow(item, user))
                                            .toArray(String[][]::new);

        return FlipTable.of(header, content);
    }

    private String[] buildRow(final Item item, final Entry user) {
        return new String[]{ item.getName(), String.valueOf(item.getSaturation()), String.valueOf(user.getItem(item.getName())) };
    }
}