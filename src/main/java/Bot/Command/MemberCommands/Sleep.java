package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.*;
import Bot.Database.IDatabase;
import Bot.Utils.Error;
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
			channel.sendMessage(Error.NOT_INITIALIZED.getMessage(ctx.getPrefix(), getName())).queue();
			return;
		}

		if (args.isEmpty()) {
			channel.sendMessage(Error.MISSING_ARGS.getMessage(ctx.getPrefix(), getName())).queue();
			return;
		}

		if (Cooldown.isActive(Activity.SLEEP, authorID, channel)) {
			return;
		}

		int energy = IDatabase.INSTANCE.getStat(authorID, Stat.ENERGY);
		if (energy == 100) {
			channel.sendMessage(Emote.REDCROSS + " The energy of your alpaca is already at the maximum").queue();
			return;
		}

		try {
			final int duration = Integer.parseInt(args.get(0));
			if (duration < 1 || duration > 100) {
				channel.sendMessage(Emote.REDCROSS + " Please enter a number between 1 - 100").queue();
				return;
			}

			energy = energy + duration > 100 ? 100 - energy : duration;
			final long cooldown = System.currentTimeMillis() + 1000L * 60 * energy;

			IDatabase.INSTANCE.setStat(authorID, Stat.ENERGY, energy);
			IDatabase.INSTANCE.setCooldown(authorID, Activity.SLEEP, cooldown);

			channel.sendMessage("\uD83D\uDCA4 Your alpaca goes to bed for **" + energy + "** minutes and rests well **Energy + " + energy + "**").queue();
		}
		catch (NumberFormatException error) {
			channel.sendMessage(Error.NaN.getMessage(ctx.getPrefix(), getName())).queue();
		}
	}

	@Override
	public String getName() {
		return "sleep";
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.MEMBER;
	}

	@Override
	public EnumSet<Permission> getRequiredPermissions() {
		return EnumSet.of(Permission.MESSAGE_WRITE);
	}

	@Override
	public String getSyntax() {
		return "sleep [1-100]";
	}

	@Override
	public String getExample() {
		return "sleep 85";
	}

	@Override
	public String getDescription() {
		return "Lets your alpaca sleep for the specified duration";
	}
}
