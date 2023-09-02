package bot.commands.member;

import bot.commands.types.UserCommand;
import bot.db.IDatabase;
import bot.models.user.User;
import bot.shop.ItemManager;
import bot.utils.CommandType;
import bot.localization.LocalizedResponse;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;
import java.util.Locale;

public class Buy extends UserCommand {
    private final ItemManager itemManager;

    public Buy(final ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final User user) {
        // selected amount
        final var amount = event.getOption("amount").getAsInt();

        // selected item
        final var item = this.itemManager.getItem(event.getOption("item").getAsString());

        // total price
        final var price = amount * item.getPrice();

        // current balance
        final var balance = user.getInventory().getCurrency();

        // sufficient balance?
        if (balance - price < 0) {
            event.reply(LocalizedResponse.get("balance.error.balanceNotSufficient", locale)).setEphemeral(true).queue();
            return;
        }

        // update db
        user.getInventory().setCurrency(balance - price);
        user.getInventory().getItems().put(item.getName(), amount);

        IDatabase.INSTANCE.updateUser(user);

        // reply to the user
        event.reply(LocalizedResponse.get("buy.successful", locale, amount, item.getName(), price)).queue();
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