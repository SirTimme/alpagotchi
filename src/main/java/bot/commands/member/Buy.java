package bot.commands.member;

import bot.commands.UserCommand;
import bot.db.IDatabase;
import bot.models.Entry;
import bot.shop.ItemManager;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;

import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;
import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Buy extends UserCommand {
    private final ItemManager itemManager;

    public Buy(final ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final Entry user) {
        final var amount = event.getOption("amount").getAsInt();
        final var item = this.itemManager.getItemByName(event.getOption("item").getAsString());

        final var price = amount * item.getPrice();
        final var balance = user.getCurrency();

        if (balance - price < 0) {
            final var format = new MessageFormat(Responses.get("insufficientBalance", locale));
            final var msg = format.format(new Object[]{});

            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        user.setCurrency(balance - price);
        user.setItem(item.getName(), user.getItem(item.getName()) + amount);

        IDatabase.INSTANCE.updateUser(user);

        final var format = new MessageFormat(Responses.get("buySuccessful", locale));
        final var msg = format.format(new Object[]{ amount, item.getName(), price });

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
                new OptionData(STRING, "item", "The item to buy", true).addChoices(choices),
                new OptionData(INTEGER, "amount", "The amount of items", true).setRequiredRange(1, 10)
        );

        return Commands.slash("buy", "Buys your alpaca items from the shop")
                       .addOptions(options);
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.USER;
    }
}