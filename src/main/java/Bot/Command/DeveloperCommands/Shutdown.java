package Bot.Command.DeveloperCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.PermissionLevel;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.EnumSet;

public class Shutdown implements ICommand {
	@Override
	public void execute(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();

		if (!PermissionLevel.DEVELOPER.hasPermission(ctx.getMember())) {
			channel.sendMessage("<:RedCross:782229279312314368> This is a **developer-only** command").queue();
			return;
		}

		final String botName = ctx.getJDA().getSelfUser().getName();
		channel.sendMessage("<:GreenTick:782229268914372609> " + botName + " is shutting down...").complete();

		System.exit(0);
	}

	@Override
	public String getHelp(String prefix) {
		return "**Usage:** " + prefix + "shutdown\n" +
			"**Aliases:** " + getAliases() + "\n" +
			"**Example:** " + prefix + "shutdown";
	}

	@Override
	public String getName() {
		return "shutdown";
	}

	@Override
	public Enum<PermissionLevel> getPermissionLevel() {
		return PermissionLevel.DEVELOPER;
	}

	@Override
	public EnumSet<Permission> getRequiredPermissions() {
		return EnumSet.of(Permission.MESSAGE_WRITE);
	}
}
