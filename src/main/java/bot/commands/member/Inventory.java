package bot.commands.member;

import bot.commands.types.UserCommand;
import bot.models.user.User;
import bot.shop.IConsumable;
import bot.shop.ItemManager;
import bot.commands.types.CommandType;
import bot.localization.LocalizedResponse;
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
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final User user) {
        final var balanceMsg = LocalizedResponse.get("general.embed.formattedBalance", locale, user.getInventory().getCurrency());
        final var howToBuyMsg = LocalizedResponse.get("general.embed.formattedHowToBuy", locale);

        final var embed = new EmbedBuilder()
                .setTitle(LocalizedResponse.get("inventory.embed.title", locale))
                .setDescription(balanceMsg + "\n```\n" + buildTable(user, locale) + "\n```\n" + howToBuyMsg)
                .setFooter(LocalizedResponse.get("general.embed.footNote.createdBy", locale))
                .setTimestamp(Instant.now())
                .build();

        event.replyEmbeds(embed).queue();
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

    private String buildTable(final User user, final Locale locale) {
        final var content = this.itemManager
                .getItems()
                .stream()
                .map(item -> buildRow(item, user, locale))
                .toArray(String[][]::new);

        final var localizedName = LocalizedResponse.get("inventory.item.name", locale);
        final var localizedSaturation = LocalizedResponse.get("inventory.item.saturation", locale);
        final var localizedQuantity = LocalizedResponse.get("inventory.item.quantity", locale);

        final String[] header = { localizedName, localizedSaturation, localizedQuantity };

        return FlipTable.of(header, content);
    }

    private String[] buildRow(final IConsumable item, final User user, final Locale locale) {
        final var itemName = LocalizedResponse.get("general.item." + item.getName(), locale);
        final var saturation = String.valueOf(item.getSaturation());
        final var quantity = String.valueOf(user.getInventory().getItems().get(item.getName()));

        return new String[]{ itemName, saturation, quantity };
    }
}