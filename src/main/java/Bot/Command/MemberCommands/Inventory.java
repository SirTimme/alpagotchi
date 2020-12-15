package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class Inventory implements ICommand {
    @Override
    public void handle(CommandContext commandContext) {
        final TextChannel channel = commandContext.getChannel();
        final long memberID = commandContext.getGuild().getMember(commandContext.getAuthor()).getIdLong();

        int amountofSalad = IDataBaseManager.INSTANCE.getInventory(memberID, "salad");
        int amountOfWaterbottle = IDataBaseManager.INSTANCE.getInventory(memberID, "waterbottle");
        int amountOfBattery = IDataBaseManager.INSTANCE.getInventory(memberID, "battery");

        channel.sendMessage("\uD83D\uDCE6 Your inventory contains **" +
                (amountofSalad > 1 ? amountofSalad + "** salads" : amountofSalad + "** salad") + " and **" +
                (amountOfWaterbottle > 1 ? amountOfWaterbottle + "** waterbottles" : amountOfWaterbottle + "** waterbottle") + " and **" +
                (amountOfBattery > 1 ? amountOfBattery + "** batteries" : amountOfBattery + "** battery") + "").queue();
    }

    @Override
    public String getHelp(CommandContext commandContext) {
        return "`Usage: " + IDataBaseManager.INSTANCE.getPrefix(commandContext.getGuild().getIdLong()) + "inventory\n" +
                (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") +
                "Displays your inventory with the bought items from the shop";
    }

    @Override
    public String getName() {
        return "inventory";
    }

    @Override
    public String getPermissionLevel() {
        return "member";
    }

    @Override
    public List<String> getAliases() {
        return List.of("inv");
    }
}

