package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.PermissionLevel;
import Bot.Database.IDatabase;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.EnumSet;
import java.util.List;

public class Sleep implements ICommand {
	@Override
	public void execute(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final long authorID = ctx.getAuthorID();
		final List<String> args = ctx.getArgs();

		if (!IDatabase.INSTANCE.isUserInDB(authorID)) {
			channel.sendMessage("<:RedCross:782229279312314368> You don't own an alpaca, use **" + ctx.getPrefix() + "init** first").queue();
			return;
		}

		if (args.isEmpty()) {
			channel.sendMessage("<:RedCross:782229279312314368> Couldn't resolve the sleep duration").queue();
			return;
		}

		final long sleepCooldown = IDatabase.INSTANCE.getCooldown(authorID, "sleep") - System.currentTimeMillis();
		if (sleepCooldown > 0) {
			channel.sendMessage("<:RedCross:782229279312314368> Your alpaca is already sleeping").queue();
			return;
		}

		final int energy = IDatabase.INSTANCE.getAlpacaValues(authorID, "energy");
		if (energy == 100) {
			channel.sendMessage("<:RedCross:782229279312314368> The energy of your alpaca is already at the maximum").queue();
			return;
		}

		try {
			final int duration = Integer.parseInt(args.get(0));
			if (duration > 120) {
				channel.sendMessage("Your alpaca can sleep max. 120 minutes at a time").queue();
				return;
			}

			final int newEnergy = energy + duration / 2 > 100 ? 100 - energy : duration / 2;
			final long newCooldown = System.currentTimeMillis() + 1000L * 60 * 2 * newEnergy;

			IDatabase.INSTANCE.setAlpacaValues(authorID, "energy", newEnergy);
			IDatabase.INSTANCE.setCooldown(authorID, "sleep", newCooldown);

			channel.sendMessage("\uD83D\uDCA4 Your alpaca goes to bed for **" + newEnergy * 2 + "** minutes and rests well **Energy + " + newEnergy + "**").queue();
		}
		catch (NumberFormatException error) {
			channel.sendMessage("<:RedCross:782229279312314368> Couldn't resolve the sleep duration").queue();
		}
	}

	@Override
	public String getHelp(String prefix) {
		return "**Usage:** " + prefix + "sleep [1-120]\n**Aliases:** " + getAliases() + "\n**Example:** " + prefix + "sleep 56";
	}

	@Override
	public String getName() {
		return "sleep";
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
