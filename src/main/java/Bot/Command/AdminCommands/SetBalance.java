package Bot.Command.AdminCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Config;
import Bot.Utils.Emote;
import Bot.Utils.Error;
import Bot.Utils.PermissionLevel;
import Bot.Database.IDatabase;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.EnumSet;
import java.util.List;

public class SetBalance implements ICommand {
	@Override
	public void execute(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final List<String> args = ctx.getArgs();
		final String prefix = ctx.getPrefix();

		if (!PermissionLevel.ADMIN.hasPermission(ctx.getMember())) {
			channel.sendMessage(Emote.REDCROSS + " This is an **admin-only** command, you're missing the **Manage Server** permission").queue();
			return;
		}

		if (args.isEmpty() || args.size() < 2) {
			channel.sendMessage(Error.MISSING_ARGS.getMessage(prefix, getName())).queue();
			return;
		}

		final List<User> mentionedUser = ctx.getMessage().getMentionedUsers();
		if (mentionedUser.isEmpty()) {
			channel.sendMessage(Error.MISSING_ARGS.getMessage(prefix, getName())).queue();
			return;
		}

		final User user = mentionedUser.get(0);
		final long userID = user.getIdLong();

		if (!IDatabase.INSTANCE.isUserInDB(userID)) {
			channel.sendMessage(Emote.REDCROSS + " The mentioned user doesn't own an alpaca, he's to use **" + prefix + "init** first").queue();
			return;
		}

		try {
			final int newBalance = Integer.parseInt(args.get(1));
			final int currentBalance = IDatabase.INSTANCE.getBalance(userID);

			IDatabase.INSTANCE.setBalance(userID, newBalance - currentBalance);

			channel.sendMessage("\uD83D\uDCB3 The balance of **" + user.getName() + "** has been set to **" + newBalance + "**").queue();
		}
		catch (NumberFormatException error) {
			channel.sendMessage(Error.NaN.getMessage(prefix, getName())).queue();
		}
	}

	@Override
	public String getName() {
		return "setbalance";
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.ADMIN;
	}

	@Override
	public EnumSet<Permission> getRequiredPermissions() {
		return EnumSet.of(Permission.MESSAGE_WRITE);
	}

	@Override public String getSyntax() {
		return "setbalance [@user] [balance]";
	}

	@Override public String getExample() {
		return "setbalance <@" + Config.get("BOT_ID") + "> 150";
	}

	@Override public String getDescription() {
		return "Sets the balance of a specific user";
	}
}
