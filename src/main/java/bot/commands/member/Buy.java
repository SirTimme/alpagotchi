package bot.commands.member;

import bot.commands.UserCommand;
import bot.db.IDatabase;
import bot.models.User;
import bot.shop.ItemManager;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Buy extends UserCommand {
    private final ItemManager itemManager;

    public Buy(final ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final User user) {
        // Selected amount
        final var amountChoice =  Objects.requireNonNull(event.getOption("amount"));
        final var amount = amountChoice.getAsInt();

        // Selected item
        final var itemChoice = Objects.requireNonNull(event.getOption("item"));
        final var item = this.itemManager.getItem(itemChoice.getAsString());

        // Total price
        final var price = amount * item.getPrice();

        // Current balance
        final var balance = user.getInventory().getCurrency();

        // Enough balance?
        if (balance - price < 0) {
            final var msg = Responses.getLocalizedResponse("balanceNotSufficient", locale);

            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        // Update db
        user.getInventory().setCurrency(balance - price);

        switch (item.getName()) {
            case "salad" ->  {
                var oldAmount = user.getInventory().getSalad();
                user.getInventory().setSalad(oldAmount + amount);
            }
            case "taco" ->  {
                var oldAmount = user.getInventory().getTaco();
                user.getInventory().setTaco(oldAmount + amount);
            }
            case "steak" ->  {
                var oldAmount = user.getInventory().getSteak();
                user.getInventory().setSteak(oldAmount + amount);
            }
            case "water" ->  {
                var oldAmount = user.getInventory().getWater();
                user.getInventory().setWater(oldAmount + amount);
            }
            case "lemonade" ->  {
                var oldAmount = user.getInventory().getLemonade();
                user.getInventory().setLemonade(oldAmount + amount);
            }
            case "cacao" ->  {
                var oldAmount = user.getInventory().getCacao();
                user.getInventory().setCacao(oldAmount + amount);
            }
        }

        IDatabase.INSTANCE.updateUser(user);

        final var format = new MessageFormat(Responses.getLocalizedResponse("buySuccessful", locale));
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
                new OptionData(OptionType.STRING, "item", "The item to buy", true)
                        .setDescriptionLocalization(DiscordLocale.GERMAN, "Das zu kaufende Item")
                        .addChoices(choices),
                new OptionData(OptionType.INTEGER, "amount", "The amount of items", true)
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