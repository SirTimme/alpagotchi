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

public class Shop extends UserCommand {
    private final ItemManager itemManager;

    public Shop(final ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final Entry user) {
        event.getJDA().retrieveUserById(Env.get("DEV_ID")).queue(dev -> {
            final var embed = new EmbedBuilder()
                    .setTitle("Shop")
                    .setDescription("```ansi\nCurrent Balance: \u001B[1;34m" + user.getCurrency() + " Fluffies\n```\n```\n" + buildTable() + "\n```\n```ansi\nHow to buy: \u001B[1;34m/buy <item> <amount>\n```")
                    .setFooter(Responses.get("createdByNotice", locale), dev.getAvatarUrl())
                    .setTimestamp(Instant.now())
                    .build();

            event.replyEmbeds(embed).queue();
        });
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("shop", "Shows the shop")
                       .setDescriptionLocalization(DiscordLocale.GERMAN, "Zeigt den Laden");
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.INFO;
    }

    private String buildTable() {
        final String[] header = { "Name", "Price", "Saturation" };
        final var content = this.itemManager.getItems()
                                            .stream()
                                            .map(this::buildRow)
                                            .toArray(String[][]::new);

        return FlipTable.of(header, content);
    }

    private String[] buildRow(final Item item) {
        return new String[]{ item.getName(), String.valueOf(item.getPrice()), String.valueOf(item.getSaturation()) };
    }
}