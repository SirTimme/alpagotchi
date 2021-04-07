package Bot.Command.DeveloperCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.Emote;
import Bot.Utils.Error;
import Bot.Utils.PermissionLevel;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.EnumSet;

public class Shutdown implements ICommand {
	@Override
	public void execute(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();

		if (!PermissionLevel.DEVELOPER.hasPermission(ctx.getMember())) {
			channel.sendMessage(Error.DEV_ONLY.getMessage(ctx.getPrefix(), getName())).queue();
			return;
		}

		channel.sendMessage(Emote.GREENTICK + " **Alpagotchi** is shutting down...").queue();

		ctx.getJDA().shutdown();
		System.exit(0);
	}

	@Override
	public String getName() {
		return "shutdown";
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.DEVELOPER;
	}

	@Override
	public EnumSet<Permission> getRequiredPermissions() {
		return EnumSet.of(Permission.MESSAGE_WRITE);
	}

	@Override
	public String getSyntax() {
		return "shutdown";
	}

	@Override
	public String getDescription() {
		return "Shutdowns the bot";
	}
}
