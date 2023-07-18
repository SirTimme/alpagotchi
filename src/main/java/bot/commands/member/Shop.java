package bot.commands.member;

import bot.commands.UserSlashCommand;
import bot.models.User;
import bot.shop.IConsumable;
import bot.shop.ItemManager;
import bot.utils.CommandType;
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

public class Shop extends UserSlashCommand {
    private final ItemManager itemManager;

    public Shop(final ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final User user) {
        event.getJDA().retrieveUserById(System.getenv("DEV_ID")).queue(dev -> {
            final var format = new MessageFormat(Responses.getLocalizedResponse("formattedCurrentBalance", locale));
            final var msg = format.format(new Object[]{ user.getInventory().getCurrency() });

            final var embed = new EmbedBuilder()
                    .setTitle(Responses.getLocalizedResponse("shopEmbedTitle", locale))
                    .setDescription(msg + "\n```\n" + buildTable(user, locale) + "\n```\n" + Responses.getLocalizedResponse("formattedHowToBuy", locale))
                    .setFooter(Responses.getLocalizedResponse("createdByNotice", locale), dev.getAvatarUrl())
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

    private String buildTable(final User user, final Locale locale) {
        final var content = this.itemManager
                .getItems()
                .stream()
                .map(item -> buildRow(item, user, locale))
                .toArray(String[][]::new);

        final var localizedName = Responses.getLocalizedResponse("inventoryName", locale);
        final var localizedPrice = Responses.getLocalizedResponse("inventoryPrice", locale);
        final var localizedQuantity = Responses.getLocalizedResponse("inventoryQuantity", locale);

        final String[] header = { localizedName, localizedPrice, localizedQuantity };

        return FlipTable.of(header, content);
    }

    private String[] buildRow(final IConsumable item, final User user, final Locale locale) {
        final var itemName = Responses.getLocalizedResponse(item.getName(), locale);
        final var saturation = String.valueOf(item.getPrice());
        final var quantity = user.getInventory().getItems().get(item.getName());

        return new String[]{ itemName, saturation, String.valueOf(quantity) };
    }
}