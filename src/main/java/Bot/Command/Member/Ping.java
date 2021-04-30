package Bot.Command.Member;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.Level;
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
	public String getName() {
		return "ping";
	}

	@Override
	public Level getLevel() {
		return Level.MEMBER;
	}

	@Override
	public EnumSet<Permission> getCommandPerms() {
		return EnumSet.of(Permission.MESSAGE_WRITE);
	}

	@Override
	public String getSyntax() {
		return "ping";
	}

	@Override
	public String getDescription() {
		return "Displays the current ping of the bot";
	}
}
