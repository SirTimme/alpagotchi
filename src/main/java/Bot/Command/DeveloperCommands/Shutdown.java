package Bot.Command.DeveloperCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.Emote;
import Bot.Utils.PermLevel;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.EnumSet;

public class Shutdown implements ICommand {
	@Override
	public void execute(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();

		channel.sendMessage(Emote.GREENTICK + " **Alpagotchi** is shutting down...").complete();

		ctx.getJDA().shutdown();
		System.exit(0);
	}

	@Override
	public String getName() {
		return "shutdown";
	}

	@Override
	public PermLevel getPermLevel() {
		return PermLevel.DEVELOPER;
	}

	@Override
	public EnumSet<Permission> getCommandPerms() {
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
