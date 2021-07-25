package Bot.Command.Member;

import Bot.Command.ISlashCommand;
import Bot.Models.User;
import Bot.Database.IDatabase;
import Bot.Shop.Item;
import Bot.Shop.ItemManager;
import Bot.Utils.Emote;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class Gift implements ISlashCommand {
    private final ItemManager itemMan;

    public Gift(ItemManager itemMan) {
        this.itemMan = itemMan;
    }

    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        final User authorUser = IDatabase.INSTANCE.getUser(authorID);

        if (authorUser == null) {
            event.reply(Emote.REDCROSS + " You don't own an alpaca, use **/init** first")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final net.dv8tion.jda.api.entities.User user = event.getOption("user").getAsUser();

        if (user.getIdLong() == authorID) {
            event.reply(Emote.REDCROSS + " You can't gift yourself items")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final User giftedUserUser = IDatabase.INSTANCE.getUser(user.getIdLong());

        if (giftedUserUser == null) {
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

        if (authorUser.getInventory().getItem(item.getName()) - amount < 0) {
            event.reply(Emote.REDCROSS + " You don't own that many items to gift")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        authorUser.getInventory().setItem(item.getName(), -amount);
        giftedUserUser.getInventory().setItem(item.getName(), amount);

        IDatabase.INSTANCE.setUser(authorID, authorUser);
        IDatabase.INSTANCE.setUser(user.getIdLong(), giftedUserUser);

        event.reply("\uD83C\uDF81 You successfully gifted **" + amount + " " + item.getName() + "** to **" + user.getName() + "**")
             .queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("gift", "Gifts another user items")
                .addOptions(
                        new OptionData(USER, "user", "The user you want to gift to", true),
                        new OptionData(STRING, "item", "The item to gift", true)
                                .addChoices(
                                        new Command.Choice("salad", "salad"),
                                        new Command.Choice("taco", "taco"),
                                        new Command.Choice("steak", "steak"),
                                        new Command.Choice("water", "water"),
                                        new Command.Choice("lemonade", "lemonade"),
                                        new Command.Choice("cacao", "cacao")
                                ),
                        new OptionData(INTEGER, "amount", "The amount of gifted items", true)
                );
    }
}
