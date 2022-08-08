package bot.commands.member;

import bot.commands.UserCommand;
import bot.db.IDatabase;
import bot.models.Entry;
import bot.shop.ItemManager;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;
import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Buy extends UserCommand {
    private final ItemManager itemManager;

    public Buy(final ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final Entry user) {
        // Selected amount
        final var amountChoice =  Objects.requireNonNull(event.getOption("amount"));
        final var amount = amountChoice.getAsInt();

        // Selected item
        final var itemChoice = Objects.requireNonNull(event.getOption("item"));
        final var item = this.itemManager.getItem(itemChoice.getAsString());

        // Total price
        final var price = amount * item.price();

        // Current balance
        final var balance = user.getCurrency();

        // Enough balance?
        if (balance - price < 0) {
            final var msg = Responses.get("balanceNotSufficient", locale);

            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        // Update db
        user.setCurrency(balance - price);
        user.setItem(item.name(), user.getItem(item.name()) + amount);
        IDatabase.INSTANCE.updateUser(user);

        final var format = new MessageFormat(Responses.get("buySuccessful", locale));
        final var msg = format.format(new Object[]{ amount, item.name(), price });

        event.reply(msg).queue();
    }

    @Override
    public CommandData getCommandData() {
        final var choices = List.of(
                new Command.Choice("salad", "salad"),
                new Command.Choice("taco", "taco"),
                new Command.Choice("steak", "steak"),
                new Command.Choice("water", "water"),
                new Command.Choice("lemonade", "lemonade"),
                new Command.Choice("cacao", "cacao")
        );

        final var options = List.of(
                new OptionData(STRING, "item", "The item to buy", true)
                        .setDescriptionLocalization(DiscordLocale.GERMAN, "Das zu kaufende Item")
                        .addChoices(choices),
                new OptionData(INTEGER, "amount", "The amount of items", true)
                        .setDescriptionLocalization(DiscordLocale.GERMAN, "Die Anzahl an Items")
                        .setRequiredRange(1, 10)
        );

        return Commands.slash("buy", "Buys items from the shop")
                       .setDescriptionLocalization(DiscordLocale.GERMAN, "Kauft Items von dem Laden")
                       .addOptions(options);
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.USER;
    }
}