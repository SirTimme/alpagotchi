package Bot.Command.Member;

import Bot.Command.ISlashCommand;
import Bot.Models.Entry;
import Bot.Database.IDatabase;
import Bot.Shop.Item;
import Bot.Shop.ItemManager;
import Bot.Utils.Emote;
import Bot.Utils.Language;
import Bot.Utils.Stat;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.concurrent.TimeUnit;

public class Feed implements ISlashCommand {
    private final ItemManager itemMan;

    public Feed(ItemManager itemMan) {
        this.itemMan = itemMan;
    }

    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        Entry entry = IDatabase.INSTANCE.getEntry(authorID);

        if (entry == null) {
            event.reply(Emote.REDCROSS + " You don't own an alpaca, use **/init** first")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        long sleep = TimeUnit.MILLISECONDS.toMinutes(entry.getCooldowns().getSleep() - System.currentTimeMillis());

        if (sleep > 0) {
            event.reply(Emote.REDCROSS + " Your alpaca sleeps, it'll wake up in **" + Language.handle(sleep, "minute") + "**")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final int amount = (int) event.getOption("amount").getAsLong();

        if (amount > 5) {
            event.reply(Emote.REDCROSS + " You can only feed max. 5 items at a time")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final Item item = this.itemMan.getItem(event.getOption("item").getAsString());

        if (entry.getInventory().getItem(item.getName()) - amount < 0) {
            event.reply(Emote.REDCROSS + " You don't own that many items")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final int oldValue = item.getStat().equals(Stat.HUNGER) ? entry.getAlpaca().getHunger() : entry.getAlpaca().getThirst();
        final int saturation = amount * item.getSaturation();

        if (oldValue + saturation > 100) {
            event.reply(Emote.REDCROSS + " You would overfeed your alpaca")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        IDatabase.INSTANCE.setEntry(authorID, Stat.valueOf(item.getName().toUpperCase()), -amount);
        IDatabase.INSTANCE.setEntry(authorID, item.getStat(), saturation);

        if (item.getStat().equals(Stat.HUNGER)) {
            event.reply(":meat_on_bone: Your alpaca eats the **" + Language.handle(amount, item.getName()) + "** in one bite **Hunger + " + saturation + "**").queue();
        }
        else {
            event.reply(":beer: Your alpaca drinks the **" + Language.handle(amount, item.getName()) + "** empty **Thirst + " + saturation + "**").queue();
        }
    }
}
