package Bot.Command.Member;

import Bot.Command.ISlashCommand;
import Bot.Models.User;
import Bot.Database.IDatabase;
import Bot.Shop.Item;
import Bot.Shop.ItemManager;
import Bot.Utils.Emote;
import Bot.Utils.Language;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;
import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Buy implements ISlashCommand {
    private final ItemManager itemMan;

    public Buy(ItemManager itemMan) {
        this.itemMan = itemMan;
    }

    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        User user = IDatabase.INSTANCE.getUser(authorID);
        if (user == null) {
            event.reply(Emote.REDCROSS + " You don't own an alpaca, use **/init** first")
                 .setEphemeral(true)
                 .queue();
            return;
        }
        final Item item = this.itemMan.getItem(event.getOption("item").getAsString());
        final int balance = user.getInventory().getCurrency();
        final int amount = (int) event.getOption("amount").getAsLong();
        if (amount > 10) {
            event.reply(Emote.REDCROSS + " You can purchase max. 10 items at a time")
                 .setEphemeral(true)
                 .queue();
            return;
        }
        final int price = amount * item.getPrice();
        if (balance - price < 0) {
            event.reply(Emote.REDCROSS + " Insufficient amount of fluffies")
                 .setEphemeral(true)
                 .queue();
            return;
        }
        user.getInventory().setCurrency(-price);
        user.getInventory().setItem(item.getName(), amount);
        IDatabase.INSTANCE.setUser(authorID, user);

        event.reply(":moneybag: You successfully bought **" + Language.handle(amount, item.getName()) + "** for **" + price + "** fluffies").queue();
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
