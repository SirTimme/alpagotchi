package bot.commands.member;

import bot.commands.UserCommand;
import bot.models.User;
import bot.shop.IConsumable;
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

import java.text.MessageFormat;
import java.time.Instant;
import java.util.Locale;

public class Inventory extends UserCommand {
    private final ItemManager itemManager;

    public Inventory(final ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final User user) {
        event.getJDA().retrieveUserById(Env.get("DEV_ID")).queue(dev -> {
            final var balanceFormat = new MessageFormat(Responses.getLocalizedResponse("formattedCurrentBalance", locale));
            final var balanceMsg = balanceFormat.format(new Object[]{ user.getCurrency() });

            final var embed = new EmbedBuilder()
                    .setTitle(Responses.getLocalizedResponse("inventoryEmbedTitle", locale))
                    .setDescription(balanceMsg + "\n```\n" + buildTable(user, locale) + "\n```\n" + Responses.getLocalizedResponse("formattedHowToBuy", locale))
                    .setFooter(Responses.getLocalizedResponse("createdByNotice", locale), dev.getAvatarUrl())
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

    private String buildTable(final Entry user, final Locale locale) {
        final var content = this.itemManager
                .getItems()
                .stream()
                .map(item -> buildRow(item, user, locale))
                .toArray(String[][]::new);

        final var localizedName = Responses.getLocalizedResponse("inventoryName", locale);
        final var localizedSaturation = Responses.getLocalizedResponse("inventorySaturation", locale);
        final var localizedQuantity = Responses.getLocalizedResponse("inventoryQuantity", locale);

        final String[] header = { localizedName, localizedSaturation, localizedQuantity };

        return FlipTable.of(header, content);
    }

    private String[] buildRow(final IConsumable item, final Entry user, final Locale locale) {
        final var itemName = Responses.getLocalizedResponse(item.getName(), locale);
        final var saturation = String.valueOf(item.getSaturation());
        final var quantity = String.valueOf(user.getItem(item.getName()));

        return new String[]{ itemName, saturation, quantity };
    }
}