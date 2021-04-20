package Bot.Command.DevCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.Emote;
import Bot.Utils.PermLevel;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.Permission;

import java.util.EnumSet;

public class Shutdown implements ICommand {
	@Override
	public void execute(CommandContext ctx) {
		ctx.getChannel().sendMessage(Emote.GREENTICK + " **Alpagotchi** is shutting down...").queue();

		ctx.getJDA().shutdown();
		BotCommons.shutdown(ctx.getJDA());
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
