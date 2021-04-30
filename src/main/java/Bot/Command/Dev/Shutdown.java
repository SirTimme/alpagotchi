package Bot.Command.Dev;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.Emote;
import Bot.Utils.Level;
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
	public Level getLevel() {
		return Level.DEVELOPER;
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
