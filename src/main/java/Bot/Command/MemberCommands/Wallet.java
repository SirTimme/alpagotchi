package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.entities.TextChannel;

public class Wallet implements ICommand {

    @Override
    public void handle(CommandContext commandContext) {
        long memberID = commandContext.getGuild().getMember(commandContext.getAuthor()).getIdLong();
        TextChannel channel = commandContext.getChannel();

        channel.sendMessage("\uD83D\uDCB5 Your current balance is **" + IDataBaseManager.INSTANCE.getInventory(memberID, "currency") + "** fluffies").queue();
    }

    @Override
    public String getHelp(CommandContext commandContext) {
        return "`Usage: " + IDataBaseManager.INSTANCE.getPrefix(commandContext.getGuild().getIdLong()) + "wallet`\nShows your current balance of fluffies";
    }

    @Override
    public String getName() {
        return "wallet";
    }

    @Override
    public String getPermissionLevel() {
        return "member";
    }
}
