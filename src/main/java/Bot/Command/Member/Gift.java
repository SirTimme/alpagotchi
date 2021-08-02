package Bot.Command.Member;

import Bot.Command.IUserCommand;
import Bot.Models.DBUser;
import Bot.Database.IDatabase;
import Bot.Shop.Item;
import Bot.Shop.ItemManager;
import Bot.Utils.Language;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static Bot.Utils.Emote.REDCROSS;
import static Bot.Utils.Language.PLURAL;
import static Bot.Utils.Language.SINGULAR;
import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class Gift implements IUserCommand {
    private final ItemManager itemMan;

    public Gift(ItemManager itemMan) {
        this.itemMan = itemMan;
    }

    @Override
    public void execute(SlashCommandEvent event, DBUser user) {
        final User userChoice = event.getOption("user").getAsUser();
        if (userChoice.getIdLong() == user.getId()) {
            event.reply(REDCROSS + " You can't gift yourself items")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final DBUser giftedUserDBUser = IDatabase.INSTANCE.getUser(userChoice.getIdLong());
        if (giftedUserDBUser == null) {
            event.reply(REDCROSS + " The mentioned user doesn't own an alpaca, he's to use **/init** first")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final int amount = (int) event.getOption("amount").getAsLong();
        if (amount > 5) {
            event.reply(REDCROSS + " You can gift max. 5 items at a time")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final String itemChoice = event.getOption("item").getAsString();
        final Item item = itemMan.getItem(itemChoice);

        if (user.getInventory().getItem(item.getName(SINGULAR)) - amount < 0) {
            event.reply(REDCROSS + " You don't own that many items to gift")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        user.getInventory().setItem(item.getName(SINGULAR), -amount);
        giftedUserDBUser.getInventory().setItem(item.getName(SINGULAR), amount);

        IDatabase.INSTANCE.setUser(user.getId(), user);
        IDatabase.INSTANCE.setUser(userChoice.getIdLong(), giftedUserDBUser);

        event.reply("\uD83C\uDF81 You successfully gifted **" + amount + " " + Language.handle(amount, item.getName(SINGULAR), item.getName(PLURAL)) + "** to **" + userChoice.getName() + "**")
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
