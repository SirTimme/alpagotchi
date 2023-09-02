package bot.commands.member;

import bot.commands.types.UserCommand;
import bot.models.user.User;
import bot.shop.IConsumable;
import bot.shop.ItemManager;
import bot.utils.CommandType;
import bot.localization.LocalizedResponse;
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
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final User user) {
        final var balanceMsg = LocalizedResponse.get("general.embed.formattedBalance", locale, user.getInventory().getCurrency());
        final var howToBuyMsg = LocalizedResponse.get("general.embed.formattedHowToBuy", locale);

        final var embed = new EmbedBuilder()
                .setTitle(LocalizedResponse.get("shop.embed.title", locale))
                .setDescription(balanceMsg + "\n```\n" + buildTable(user, locale) + "\n```\n" + howToBuyMsg)
                .setFooter(LocalizedResponse.get("general.embed.footNote.createdBy", locale))
                .setTimestamp(Instant.now())
                .build();

        event.replyEmbeds(embed).queue();
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

    private String buildTable(final User user, final Locale locale) {
        final var content = this.itemManager
                .getItems()
                .stream()
                .map(item -> buildRow(item, user, locale))
                .toArray(String[][]::new);

        final var localizedName = LocalizedResponse.get("inventory.item.name", locale);
        final var localizedPrice = LocalizedResponse.get("inventory.item.price", locale);
        final var localizedQuantity = LocalizedResponse.get("inventory.item.quantity", locale);

        final String[] header = { localizedName, localizedPrice, localizedQuantity };

        return FlipTable.of(header, content);
    }

    private String[] buildRow(final IConsumable item, final User user, final Locale locale) {
        final var itemName = LocalizedResponse.get("general.item." + item.getName(), locale);
        final var saturation = String.valueOf(item.getPrice());
        final var quantity = user.getInventory().getItems().get(item.getName());

        return new String[]{ itemName, saturation, String.valueOf(quantity) };
    }
}