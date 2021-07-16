package Bot.Command.Dev;

import Bot.Command.ISlashCommand;
import Bot.Config;
import Bot.Utils.Emote;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;
import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Update implements ISlashCommand {
    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        event.getJDA()
             .updateCommands()
             .addCommands(
                     new CommandData("ping", "Displays the current latency of Alpagotchi"),
                     new CommandData("init", "Initializes a new alpaca"),
                     new CommandData("balance", "Shows your fluffy balance"),
                     new CommandData("buy", "Buys your alpaca items from the shop")
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
                             ),
                     new CommandData("count", "Counts all alpacas of Alpagotchi"),
                     new CommandData("delete", "Deletes your personal data"),
                     new CommandData("feed", "Feeds your alpaca items")
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
                             ),
                     new CommandData("gift", "Gifts another user items")
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
                             ),
                     new CommandData("image", "Search pixabay with your query")
                             .addOptions(
                                     new OptionData(STRING, "query", "The query you want to search", true)
                             ),
                     new CommandData("work", "Lets your alpaca work for fluffies"),
                     new CommandData("myalpaca", "Shows your alpaca with its stats"),
                     new CommandData("nick", "Gives your alpaca a new nickname")
                             .addOptions(
                                     new OptionData(STRING, "nickname", "The new nickname of the alpaca", true)
                             ),
                     new CommandData("help", "Shows all commands or additional help for a specific"),
                     new CommandData("sleep", "Lets your alpaca sleep for the specified duration to regain energy")
                             .addOptions(
                                     new OptionData(INTEGER, "duration", "The duration in minutes", true)
                             ),
                     new CommandData("outfit", "Changes the appearance of your alpaca").addOptions(
                             new OptionData(STRING, "outfit", "The new outfit of your alpaca", true)
                                     .addChoices(
                                             new Command.Choice("default", "default"),
                                             new Command.Choice("gentleman", "gentleman")
                                     )
                     ),
                     new CommandData("pet", "Pets your alpaca to gain joy").addOptions(
                             new OptionData(STRING, "spot", "The spot where you want to pet your alpaca", true)
                                     .addChoices(
                                             new Command.Choice("neck", "neck"),
                                             new Command.Choice("head", "head"),
                                             new Command.Choice("tail", "tail"),
                                             new Command.Choice("leg", "leg"),
                                             new Command.Choice("back", "back")
                                     )
                     ),
                     new CommandData("inventory", "Shows your items for your alpaca"),
                     new CommandData("shop", "Shows the shop with all the items to buy")).queue();

        event.getGuild()
             .updateCommands()
             .addCommands(
                     new CommandData("decrease", "Let the alpacas lose stats")
                             .addOptions(
                                     new OptionData(STRING, "status", "Status of decreasing", true)
                                             .addChoices(
                                                     new Command.Choice("enable", "enable"),
                                                     new Command.Choice("disable", "disable")
                                             )
                             )
                             .setDefaultEnabled(false),
                     new CommandData("update", "Refreshes all slashcommands").setDefaultEnabled(false),
                     new CommandData("shutdown", "Shutdowns Alpagotchi").setDefaultEnabled(false)
             )
             .queue();

        event.getGuild()
             .updateCommandPrivilegesById(862727791070281732L, CommandPrivilege.enableUser(Config.get("DEV_ID")))
             .queue();

        event.getGuild()
             .updateCommandPrivilegesById(862727791070281731L, CommandPrivilege.enableUser(Config.get("DEV_ID")))
             .queue();

        event.getGuild()
             .updateCommandPrivilegesById(862727791070281730L, CommandPrivilege.enableUser(Config.get("DEV_ID")))
             .queue();

        event.reply(Emote.GREENTICK + " Successfully refreshed all slash commands").queue();
    }
}
