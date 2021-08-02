package Bot.Command.Member;

import Bot.Command.IUserCommand;
import Bot.Models.DBUser;
import Bot.Database.IDatabase;
import Bot.Shop.Item;
import Bot.Shop.ItemManager;
import Bot.Utils.Emote;
import Bot.Utils.Language;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static Bot.Utils.Language.PLURAL;
import static Bot.Utils.Language.SINGULAR;
import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;
import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Buy implements IUserCommand {
    private final ItemManager itemMan;

    public Buy(ItemManager itemMan) {
        this.itemMan = itemMan;
    }

    @Override
    public void execute(SlashCommandEvent event, DBUser user) {
        final int amount = (int) event.getOption("amount").getAsLong();
        if (amount > 10) {
            event.reply(Emote.REDCROSS + " You can purchase max. 10 items at a time")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final String itemChoice = event.getOption("item").getAsString();
        final Item item = itemMan.getItem(itemChoice);

        final int price = amount * item.getPrice();
        final int balance = user.getInventory().getCurrency();
        if (balance - price < 0) {
            event.reply(Emote.REDCROSS + " Insufficient amount of fluffies")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        user.getInventory().setCurrency(-price);
        user.getInventory().setItem(item.getName(SINGULAR), amount);
        IDatabase.INSTANCE.setUser(user.getId(), user);

        event.reply(":moneybag: You successfully bought **" + amount + " " + Language.handle(amount, item.getName(SINGULAR), item.getName(PLURAL)) + "** for **" + price + "** fluffies").queue();
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
