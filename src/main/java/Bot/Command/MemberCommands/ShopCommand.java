package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Database.IDataBaseManager;

public class ShopCommand implements ICommand {
    @Override
    public void handle(CommandContext commandContext) {

    }

    @Override
    public String getHelp(CommandContext commandContext) {
        return "`Usage: " + IDataBaseManager.INSTANCE.getPrefix(commandContext.getGuild().getIdLong()) + "shop`\nShows the shop to buy things for your alpaca";
    }

    @Override
    public String getName() {
        return "shop";
    }
}
