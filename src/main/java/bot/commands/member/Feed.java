package bot.commands.member;

import bot.commands.IUserCommand;
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

public class Feed implements IUserCommand {
    private final ItemManager itemMan;

    public Feed(ItemManager itemMan) {
        this.itemMan = itemMan;
    }

    @Override
    public Entry execute(SlashCommandEvent event, Entry user) {
        final long sleep = user.getSleep();
        if (sleep > 0) {
            event.reply(Emote.REDCROSS + " Your alpaca sleeps, it'll wake up in **" + sleep + " " + Language.handle(sleep, "minute", "minutes") + "**")
                 .setEphemeral(true)
                 .queue();
            return null;
        }

        final int amount = (int) event.getOption("amount").getAsLong();
        if (amount > 5) {
            event.reply(Emote.REDCROSS + " You can only feed max. 5 items at a time")
                 .setEphemeral(true)
                 .queue();
            return null;
        }

        final String itemChoice = event.getOption("item").getAsString();
        final Item item = this.itemMan.getItem(itemChoice);
        final int userItems = user.getItem(item.getName(SINGULAR)) - amount;

        if (userItems < 0) {
            event.reply(Emote.REDCROSS + " You don't own that many items")
                 .setEphemeral(true)
                 .queue();
            return null;
        }

        final int oldValue = item.getStat().equals("hunger") ? user.getHunger() : user.getThirst();
        final int saturation = amount * item.getSaturation();
        if (oldValue + saturation > 100) {
            event.reply(Emote.REDCROSS + " You would overfeed your alpaca")
                 .setEphemeral(true)
                 .queue();
            return null;
        }

        user.setItem(item.getName(SINGULAR), userItems);

        if (item.getStat().equals("hunger")) {
            user.setHunger(saturation);
            event.reply(":meat_on_bone: Your alpaca eats the **" + Language.handle(amount, item.getName(SINGULAR), item.getName(PLURAL)) + "** in one bite **Hunger + " + saturation + "**")
                 .queue();
        } else {
            user.setThirst(saturation);
            event.reply(":beer: Your alpaca drinks the **" + Language.handle(amount, item.getName(SINGULAR), item.getName(PLURAL)) + "** empty **Thirst + " + saturation + "**")
                 .queue();
        }

        return user;
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("feed", "Feeds your alpaca items")
                .addOptions(
                        new OptionData(STRING, "item", "The item to feed", true)
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
