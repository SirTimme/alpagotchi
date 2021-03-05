package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.PermissionLevel;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.exceptions.PermissionException;

import java.util.List;

public class Balance implements ICommand {
	@Override
	public void execute(CommandContext ctx) throws PermissionException {
		if (!IDataBaseManager.INSTANCE.isUserInDB(ctx.getAuthorID())) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> You do not own a alpaca, use **" + ctx.getPrefix() + "init** first").queue();
			return;
		}

		final int balance = IDataBaseManager.INSTANCE.getBalance(ctx.getAuthorID());

		ctx.getChannel().sendMessage("\uD83D\uDCB5 Your current balance is **" + (balance == 1 ? balance + "** fluffy" : balance + "** fluffies")).queue();
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
