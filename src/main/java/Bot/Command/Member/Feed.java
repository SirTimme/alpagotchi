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

public class Feed implements IUserCommand {
    private final ItemManager itemMan;

    public Feed(ItemManager itemMan) {
        this.itemMan = itemMan;
    }

    @Override
    public void execute(SlashCommandEvent event, DBUser user) {
        final long sleep = user.getCooldown().getSleep();
        if (sleep > 0) {
            event.reply(Emote.REDCROSS + " Your alpaca sleeps, it'll wake up in **" + sleep + " " + Language.handle(sleep, "minute", "minutes") + "**")
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

        final String itemChoice = event.getOption("item").getAsString();
        final Item item = this.itemMan.getItem(itemChoice);
        if (user.getInventory().getItem(item.getName(SINGULAR)) - amount < 0) {
            event.reply(Emote.REDCROSS + " You don't own that many items")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final int oldValue = item.getStat().equals("hunger") ? user.getAlpaca().getHunger() : user.getAlpaca().getThirst();
        final int saturation = amount * item.getSaturation();
        if (oldValue + saturation > 100) {
            event.reply(Emote.REDCROSS + " You would overfeed your alpaca")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        user.getInventory().setItem(item.getName(SINGULAR), -amount);

        if (item.getStat().equals("hunger")) {
            user.getAlpaca().setHunger(saturation);
            event.reply(":meat_on_bone: Your alpaca eats the **" + Language.handle(amount, item.getName(SINGULAR), item.getName(PLURAL)) + "** in one bite **Hunger + " + saturation + "**")
                 .queue();
        } else {
            user.getAlpaca().setThirst(saturation);
            event.reply(":beer: Your alpaca drinks the **" + Language.handle(amount, item.getName(SINGULAR), item.getName(PLURAL)) + "** empty **Thirst + " + saturation + "**")
                 .queue();
        }
        IDatabase.INSTANCE.setUser(user.getId(), user);
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
