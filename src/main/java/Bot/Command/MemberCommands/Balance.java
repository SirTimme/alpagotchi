package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.Emote;
import Bot.Utils.PermissionLevel;
import Bot.Database.IDatabase;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.EnumSet;
import java.util.List;

public class Balance implements ICommand {
	@Override
	public void execute(CommandContext ctx) {
		final long authorID = ctx.getAuthorID();
		final TextChannel channel = ctx.getChannel();

		if (!IDatabase.INSTANCE.isUserInDB(authorID)) {
			channel.sendMessage(Emote.REDCROSS + " You don't own an alpaca, use **" + ctx.getPrefix() + "init** first").queue();
			return;
		}

		final int balance = IDatabase.INSTANCE.getBalance(authorID);

		channel.sendMessage("\uD83D\uDCB5 Your current balance is **" + balance + (balance == 1 ? "** fluffy" : "** fluffies")).queue();
	}

	@Override
	public String getHelp(String prefix) {
		return "**Usage:** " + prefix + "balance\n**Aliases:** " + getAliases() + "\n**Example:** " + prefix + "wallet";
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

	@Override
	public EnumSet<Permission> getRequiredPermissions() {
		return EnumSet.of(Permission.MESSAGE_WRITE);
	}
}
