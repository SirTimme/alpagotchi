package bot.commands.member;

import bot.commands.IDynamicUserCommand;
import bot.models.Entry;
import bot.shop.Item;
import bot.shop.ItemManager;
import bot.utils.Emote;
import bot.utils.Language;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static bot.utils.Language.PLURAL;
import static bot.utils.Language.SINGULAR;
import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;
import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Buy implements IDynamicUserCommand {
    private final ItemManager itemMan;

    public Buy(ItemManager itemMan) {
        this.itemMan = itemMan;
    }

    @Override
    public Entry execute(SlashCommandEvent event, Entry user) {
        final int amount = (int) event.getOption("amount").getAsLong();
        if (amount > 10) {
            event.reply(Emote.REDCROSS + " You can buy max. 10 items at a time")
                 .setEphemeral(true)
                 .queue();
            return null;
        }

        final String itemChoice = event.getOption("item").getAsString();
        final Item item = itemMan.getItem(itemChoice);

        final int price = amount * item.getPrice();
        final int balance = user.getCurrency();
        if (balance - price < 0) {
            event.reply(Emote.REDCROSS + " Insufficient amount of fluffies")
                 .setEphemeral(true)
                 .queue();
            return null;
        }

        user.setCurrency(balance - price);
        user.setItem(item.getName(SINGULAR), user.getItem(item.getName(SINGULAR)) + amount);

        event.reply(":moneybag: You successfully bought **" + amount + " " + Language.handle(amount, item.getName(SINGULAR), item.getName(PLURAL)) + "** for **" + price + "** fluffies").queue();

        return user;
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("buy", "Buys your alpaca items from the shop")
                .addOptions(
                        new OptionData(STRING, "item", "The item to buy", true)
                                .addChoices(
                                        new Command.Choice("salad", "salad"),
                                        new Command.Choice("taco", "taco"),
                                        new Command.Choice("steak", "steak"),
                                        new Command.Choice("water", "water"),
                                        new Command.Choice("lemonade", "lemonade"),
                                        new Command.Choice("cacao", "cacao")
                                ),
                        new OptionData(INTEGER, "amount", "The amount of items", true)
                );
    }
}
