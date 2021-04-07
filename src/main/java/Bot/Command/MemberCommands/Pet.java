package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.Emote;
import Bot.Utils.Error;
import Bot.Utils.PermissionLevel;
import Bot.Database.IDatabase;
import Bot.Utils.Stat;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class Pet implements ICommand {
	private final List<String> spots = Arrays.asList("head", "tail", "leg", "neck");

	@Override
	public void execute(CommandContext ctx) {
		final long authorID = ctx.getAuthorID();
		final TextChannel channel = ctx.getChannel();
		final List<String> args = ctx.getArgs();

		if (!IDatabase.INSTANCE.isUserInDB(authorID)) {
			channel.sendMessage(Error.NOT_INITIALIZED.getMessage(ctx.getPrefix(), getName())).queue();
			return;
		}

		final int joy = IDatabase.INSTANCE.getStat(authorID, Stat.JOY);
		if (joy == 100) {
			channel.sendMessage(Emote.REDCROSS + " The joy of your alpaca is already at the maximum").queue();
			return;
		}

		if (args.isEmpty()) {
			int newJoy = (int) (Math.random() * 8 + 1);
			newJoy = newJoy + joy > 100 ? 100 - joy : newJoy;

			IDatabase.INSTANCE.setStat(authorID, Stat.JOY, newJoy);

			channel.sendMessage("\uD83E\uDD99 Your alpaca loves to spend time with you **Joy + " + newJoy + "**").queue();
			return;
		}

		final String favouriteSpot = spots.get((int) (Math.random() * 4));
		final String userSpot = args.get(0);

		final boolean validSpot = spots.contains(userSpot);

		if (!validSpot) {
			channel.sendMessage(Emote.REDCROSS + " This spot doesn't exists").queue();
			return;
		}

		if (userSpot.equals(favouriteSpot)) {
			int newJoy = (int) (Math.random() * 13 + 5);
			newJoy = newJoy + joy > 100 ? 100 - joy : newJoy;

			IDatabase.INSTANCE.setStat(authorID, Stat.JOY, newJoy);

			channel.sendMessage("\uD83E\uDD99 You found the favourite spot of your alpaca **Joy + " + newJoy + "**").queue();
		}
		else {
			int newJoy = (int) (Math.random() * 9 + 3);
			newJoy = newJoy + joy > 100 ? 100 - joy : newJoy;

			IDatabase.INSTANCE.setStat(authorID, Stat.JOY, newJoy);

			channel.sendMessage("\uD83E\uDD99 Your alpaca enjoyed the petting, but it wasn't his favourite spot **Joy + " + newJoy + "**").queue();
		}
	}

	@Override
	public String getName() {
		return "pet";
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
		return "pet (spot)";
	}

	@Override
	public String getExample() {
		return "pet head";
	}

	@Override
	public String getDescription() {
		return "Gives your alpaca a hug and restores joy";
	}
}