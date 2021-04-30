package Bot.Command.Dev;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Database.IDatabase;
import Bot.Utils.Emote;
import Bot.Utils.Error;
import Bot.Utils.Level;
import Bot.Utils.ThreadFactory;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Decrease implements ICommand {
	private boolean running = false;
	private Future<?> result;
	private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(new ThreadFactory());

	@Override
	public void execute(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final List<String> args = ctx.getArgs();
		final String prefix = ctx.getPrefix();

		if (args.isEmpty()) {
			channel.sendMessage(Error.MISSING_ARGS.getMessage(prefix, getName())).queue();
			return;
		}

		if (running && args.get(0).equalsIgnoreCase("enable")) {
			channel.sendMessage(Emote.REDCROSS + " Decreasing is already enabled").queue();
			return;
		}

		if (args.get(0).equalsIgnoreCase("enable")) {
			result = service.scheduleAtFixedRate(IDatabase.INSTANCE::decreaseValues, 2, 2, TimeUnit.HOURS);
			running = true;

			channel.sendMessage(Emote.GREENTICK + " Alpacas begin to lose stats").queue();
		}
		else {
			result.cancel(true);
			running = false;

			channel.sendMessage(Emote.REDCROSS + " Alpacas stop losing stats").queue();
		}
	}

	@Override
	public String getName() {
		return "decrease";
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
		return "decrease [enable | disable]";
	}

	@Override
	public String getExample() {
		return "decrease disable";
	}

	@Override
	public String getDescription() {
		return "Enables/Disables if the alpacas losing stats";
	}
}


