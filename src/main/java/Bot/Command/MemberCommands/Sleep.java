package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.Activity;
import Bot.Utils.Cooldown;
import Bot.Utils.Emote;
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
			channel.sendMessage(Emote.REDCROSS + " You don't own an alpaca, use **" + ctx.getPrefix() + "init** first").queue();
			return;
		}

		if (args.isEmpty()) {
			channel.sendMessage(Emote.REDCROSS + " Please specify the sleep duration").queue();
			return;
		}

		if (Cooldown.isActive(Activity.SLEEP, authorID, channel)) {
			return;
		}

		final int energy = IDatabase.INSTANCE.getAlpacaValues(authorID, "energy");
		if (energy == 100) {
			channel.sendMessage( Emote.REDCROSS + " The energy of your alpaca is already at the maximum").queue();
			return;
		}

		try {
			final int duration = Integer.parseInt(args.get(0));
			if (duration < 1 || duration > 100) {
				channel.sendMessage(Emote.REDCROSS + " Please enter a number between 1 - 120").queue();
				return;
			}

			final int newEnergy = energy + duration > 100 ? 100 - energy : duration ;
			final long newCooldown = System.currentTimeMillis() + 1000L * 60 * newEnergy;

			IDatabase.INSTANCE.setAlpacaValues(authorID, "energy", newEnergy);
			IDatabase.INSTANCE.setCooldown(authorID, "sleep", newCooldown);

			channel.sendMessage("\uD83D\uDCA4 Your alpaca goes to bed for **" + duration + "** minutes and rests well **Energy + " + newEnergy + "**").queue();
		}
		catch (NumberFormatException error) {
			channel.sendMessage(Emote.REDCROSS + " Please enter a number between 1 - 120").queue();
		}
	}

	@Override
	public String getHelp(String prefix) {
		return "**Usage:** " + prefix + "sleep [1-100]\n**Aliases:** " + getAliases() + "\n**Example:** " + prefix + "sleep 56";
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
