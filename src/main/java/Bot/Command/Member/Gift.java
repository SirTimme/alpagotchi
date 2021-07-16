package Bot.Command.Member;

import Bot.Command.ISlashCommand;
import Bot.Models.Entry;
import Bot.Database.IDatabase;
import Bot.Shop.Item;
import Bot.Shop.ItemManager;
import Bot.Utils.Emote;
import Bot.Utils.Stat;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class Gift implements ISlashCommand {
    private final ItemManager itemMan;

    public Gift(ItemManager itemMan) {
        this.itemMan = itemMan;
    }

    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        final Entry authorEntry = IDatabase.INSTANCE.getEntry(authorID);

        if (authorEntry == null) {
            event.reply(Emote.REDCROSS + " You don't own an alpaca, use **/init** first")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final User user = event.getOption("user").getAsUser();

        if (user.getIdLong() == authorID) {
            event.reply(Emote.REDCROSS + " You can't gift yourself items")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final Entry giftedUserEntry = IDatabase.INSTANCE.getEntry(user.getIdLong());

        if (giftedUserEntry == null) {
            event.reply(Emote.REDCROSS + " The mentioned user doesn't own an alpaca, he's to use **/init** first")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final int amount = (int) event.getOption("amount").getAsLong();

        if (amount > 5) {
            event.reply(Emote.REDCROSS + " You can gift max. 5 items at a time")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final Item item = this.itemMan.getItem(event.getOption("item").getAsString());

        if (authorEntry.getInventory().getItem(item.getName()) - amount < 0) {
            event.reply(Emote.REDCROSS + " You don't own that many items to gift")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        authorEntry.getInventory().setItem(item.getName(), -amount);
        giftedUserEntry.getInventory().setItem(item.getName(), amount);

        IDatabase.INSTANCE.setEntry(authorID, authorEntry);
        IDatabase.INSTANCE.setEntry(user.getIdLong(), giftedUserEntry);

        event.reply("\uD83C\uDF81 You successfully gifted **" + amount + " " + item.getName() + "** to **" + user.getName() + "**")
             .queue();
    }
}
