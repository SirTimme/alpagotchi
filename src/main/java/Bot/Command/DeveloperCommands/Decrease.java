package Bot.Command.DeveloperCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.DecreaseTask;
import Bot.Utils.Emote;
import Bot.Utils.PermissionLevel;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.EnumSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Decrease implements ICommand {
	private final Timer timer = new Timer();
	private boolean running = false;
	private TimerTask task;

	@Override
	public void execute(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final List<String> args = ctx.getArgs();

		if (!PermissionLevel.DEVELOPER.hasPermission(ctx.getMember())) {
			channel.sendMessage(Emote.REDCROSS + " This is a **developer-only** command").queue();
			return;
		}

		if (args.isEmpty()) {
			channel.sendMessage(Emote.REDCROSS + " Missing arguments").queue();
			return;
		}

		if (running && args.get(0).equalsIgnoreCase("enable")) {
			channel.sendMessage(Emote.REDCROSS + " Decreasing is already enabled").queue();
			return;
		}

		if (args.get(0).equalsIgnoreCase("enable")) {
			timer.schedule(task = new DecreaseTask(), 1000 * 7200, 1000 * 7200);
			running = true;

			channel.sendMessage( Emote.GREENTICK + " Alpacas begin to lose stats").queue();
		}
		else {
			task.cancel();
			running = false;

			channel.sendMessage( Emote.REDCROSS + " Alpacas stop losing stats").queue();
		}
	}

	@Override
	public String getHelp(String prefix) {
		return "**" + prefix + "decrease [enable | disable]**\n**Aliases:**" + getAliases() + "\n**Example:**" + prefix + "decrease disable";
	}

	@Override
	public String getName() {
		return "decrease";
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


