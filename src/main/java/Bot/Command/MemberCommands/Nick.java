package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.PermissionLevel;
import Bot.Database.IDatabase;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.EnumSet;
import java.util.List;

public class Nick implements ICommand {
	@Override
	public void execute(CommandContext ctx) {
		final long authorID = ctx.getAuthorID();
		final TextChannel channel = ctx.getChannel();
		final List<String> args = ctx.getArgs();

		if (!IDatabase.INSTANCE.isUserInDB(authorID)) {
			channel.sendMessage("<:RedCross:782229279312314368> You don't own an alpaca, " +
				"use **" + ctx.getPrefix() + "init** first")
				   .queue();
			return;
		}

		if (args.isEmpty()) {
			channel.sendMessage("<:RedCross:782229279312314368> Couldn't resolve the nickname").queue();
			return;
		}

		final String nickname = args.get(0);
		IDatabase.INSTANCE.setNickname(authorID, nickname);

		channel.sendMessage("\uD83D\uDD8A The nickname of your alpaca has been set to **" + nickname + "**").queue();
	}

	@Override
	public String getHelp(String prefix) {
		return "**Usage:** " + prefix + "nick [nickname]\n" +
			"**Aliases:** " + getAliases() + "\n" +
			"**Example:** " + prefix + "nick Fluffy";
	}

	@Override
	public String getName() {
		return "nick";
	}

	@Override
	public Enum<PermissionLevel> getPermissionLevel() {
		return PermissionLevel.MEMBER;
	}

	@Override
	public EnumSet<Permission> getRequiredPermissions() {
		return EnumSet.of(Permission.MESSAGE_WRITE);
	}
}