package bot.commands.member;

import bot.commands.interfaces.IDynamicUserCommand;
import bot.db.IDatabase;
import bot.models.Entry;
import bot.shop.Item;
import bot.shop.ItemManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;


public class Gift implements IDynamicUserCommand {
    private final ItemManager itemMan;

    public Gift(ItemManager itemMan) {
        this.itemMan = itemMan;
    }

    @Override
    public Entry execute(SlashCommandEvent event, Entry user) {
        final User userChoice = event.getOption("user").getAsUser();
        if (userChoice.getIdLong() == user.getMemberID()) {
            event.reply(REDCROSS + " You can't gift yourself items")
                 .setEphemeral(true)
                 .queue();
            return null;
        }

        final Entry giftedUserDBUser = IDatabase.INSTANCE.getUser(userChoice.getIdLong());
        if (giftedUserDBUser == null) {
            event.reply(REDCROSS + " The mentioned user doesn't own an alpaca, he's to use **/init** first")
                 .setEphemeral(true)
                 .queue();
            return null;
        }

        final int amount = (int) event.getOption("amount").getAsLong();

        if (amount > 5) {
            event.reply(REDCROSS + " You can gift max. 5 items at a time")
                 .setEphemeral(true)
                 .queue();
            return null;
        }

        final String itemChoice = event.getOption("item").getAsString();
        final Item item = itemMan.getItemByName(itemChoice);

        if (user.getItem(item.getName()) - amount < 0) {
            event.reply(REDCROSS + " You don't own that many items to gift")
                 .setEphemeral(true)
                 .queue();
            return null;
        }

        user.setItem(item.getName(), user.getItem(item.getName()) - amount);
        giftedUserDBUser.setItem(item.getName(), giftedUserDBUser.getItem(item.getName()) + amount);

        IDatabase.INSTANCE.updateUser(giftedUserDBUser);

        event.reply("\uD83C\uDF81 You successfully gifted **" + amount + " " + Language.handle(amount, item.getName(), item.getName()) + "** to **" + userChoice.getName() + "**")
             .queue();

        return user;
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
