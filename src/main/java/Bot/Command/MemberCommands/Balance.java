package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Database.IDataBaseManager;

import java.util.List;

public class Balance implements ICommand {

    @Override
    public void execute(CommandContext commandContext) {
        if (!IDataBaseManager.INSTANCE.isUserInDB(commandContext.getAuthorID())) {
            commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> You do not own a alpaca, use **" + commandContext.getPrefix() + "init** first").queue();
            return;
        }

        final int balance = IDataBaseManager.INSTANCE.getBalance(commandContext.getAuthorID());

        commandContext.getChannel().sendMessage("\uD83D\uDCB5 Your current balance is **" + (balance == 1 ? balance + "** fluffy" : balance + "** fluffies")).queue();
    }

    @Override
    public String getHelp(String prefix) {
        return "`Usage: " + prefix + "balance\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Shows your current balance of fluffies";
    }

    @Override
    public String getName() {
        return "balance";
    }

    @Override
    public Enum<PermissionLevel> getPermissionLevel() {
        return PermissionLevel.MEMBER;
    }

    @Override
    public List<String> getAliases() {
        return List.of("wallet", "money");
    }
}
