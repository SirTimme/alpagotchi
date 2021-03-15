package Bot.Command.DeveloperCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.DecreaseTask;
import Bot.Utils.PermissionLevel;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.EnumSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Decrease implements ICommand {
	private final Timer timer = new Timer();
	private boolean timerActive = false;
	private TimerTask decreaseTask;

	@Override
	public void execute(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final List<String> args = ctx.getArgs();

		if (!PermissionLevel.DEVELOPER.hasPermission(ctx.getMember())) {
			channel.sendMessage("<:RedCross:782229279312314368> This is a **developer-only** command").queue();
			return;
		}

		if (args.isEmpty()) {
			channel.sendMessage("<:RedCross:782229279312314368> Missing arguments").queue();
			return;
		}

		if (this.timerActive && args.get(0).equalsIgnoreCase("enable")) {
			channel.sendMessage("<:RedCross:782229279312314368> Decreasing is already enabled").queue();
			return;
		}

		if (args.get(0).equalsIgnoreCase("enable")) {
			this.timer.schedule(decreaseTask = new DecreaseTask(), 1000 * 7200, 1000 * 7200);
			timerActive = true;

			channel.sendMessage("<:GreenTick:782229268914372609> Alpacas begin to lose stats").queue();
		}
		else {
			this.decreaseTask.cancel();
			timerActive = false;

			channel.sendMessage("<:RedCross:782229279312314368> Alpacas stop losing stats").queue();
		}
	}

	@Override
	public String getHelp(String prefix) {
		return "**" + prefix + "decrease [enable | disable]**\n" +
			"**Aliases:**" + getAliases() + "\n" +
			"**Example:**" + prefix + "decrease disable";
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


