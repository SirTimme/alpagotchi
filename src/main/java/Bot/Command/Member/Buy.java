package Bot.Command.Member;

import Bot.Command.ISlashCommand;
import Bot.Models.User;
import Bot.Database.IDatabase;
import Bot.Shop.Item;
import Bot.Shop.ItemManager;
import Bot.Utils.Emote;
import Bot.Utils.Language;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

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
        final int amount = (int) event.getOption("amount").getAsLong();
        final int price = amount * item.getPrice();

        final int balance = user.getInventory().getCurrency();

        if (amount > 10) {
            event.reply(Emote.REDCROSS + " You can purchase max. 10 items at a time")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        if (balance - price < 0) {
            event.reply(Emote.REDCROSS + " Insufficient amount of fluffies")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        user.getInventory().setCurrency(-price);
        user.getInventory().setItem(item.getName(), amount);

        IDatabase.INSTANCE.setUser(authorID, user);

        event.reply(":moneybag: You successfully bought **" + Language.handle(amount, item.getName()) + "** for **" + price + "** fluffies")
             .queue();
    }
}
