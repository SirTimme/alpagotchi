package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Database.IDatabase;
import Bot.Utils.PermissionLevel;
import net.dv8tion.jda.api.Permission;

import java.util.EnumSet;

public class Count implements ICommand {
	@Override
	public void execute(CommandContext ctx) {
		final long totalSize = IDatabase.INSTANCE.getAll();

		ctx.getChannel().sendMessage("\uD83D\uDC65 There are **" + totalSize + "** alpacas in the farm by now").queue();
	}

	@Override
	public String getName() {
		return "count";
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.MEMBER;
	}

	@Override
	public EnumSet<Permission> getRequiredPermissions() {
		return EnumSet.of(Permission.MESSAGE_WRITE);
	}

	@Override
	public String getSyntax() {
		return "count";
	}

	@Override
	public String getDescription() {
		return "Counts all alpacas across all guilds";
	}
}
