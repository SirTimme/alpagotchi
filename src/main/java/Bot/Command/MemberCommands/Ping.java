package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.PermissionLevel;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.temporal.ChronoUnit;
import java.util.EnumSet;

public class Ping implements ICommand {
	@Override
	public void execute(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final Message message = ctx.getMessage();

		channel.sendMessage("Pinging alpacafarm...").queue((msg) -> {
			final long ping = ChronoUnit.MILLIS.between(message.getTimeCreated(), msg.getTimeCreated());

			msg.editMessage(":satellite: You reached the alpacafarm in **" + ping + "**ms").queue();
		});
	}

	@Override
	public String getHelp(String prefix) {
		return "**Usage:** " + prefix + "ping\n" +
			"**Aliases:** " + getAliases() + "\n" +
			"**Example:** " + prefix + "ping";
	}

	@Override
	public String getName() {
		return "ping";
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
