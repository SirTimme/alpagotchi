package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.*;
import Bot.Database.IDatabase;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.EnumSet;

public class Work implements ICommand {
	@Override
	public void execute(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final long authorID = ctx.getAuthorID();
		final String prefix = ctx.getPrefix();

		if (!IDatabase.INSTANCE.isUserInDB(authorID)) {
			channel.sendMessage(Emote.REDCROSS + " You don't own an alpaca, use **" + prefix + "init** first").queue();
			return;
		}

		final int energy = IDatabase.INSTANCE.getAlpacaValues(authorID, "energy");
		if (energy < 10) {
			channel.sendMessage("\uD83E\uDD71 Your alpaca is too tired to work, let it rest first with **" + prefix + "sleep**").queue();
			return;
		}

		final int joy = IDatabase.INSTANCE.getAlpacaValues(authorID, "joy");
		if (joy < 15) {
			channel.sendMessage(":pensive: Your alpaca is too sad to work, give him some love with **" + prefix + "pet**").queue();
			return;
		}

		if (Cooldown.isActive(Activity.WORK, authorID, channel) || Cooldown.isActive(Activity.SLEEP, authorID, channel)) {
			return;
		}

		final int fluffies = (int) (Math.random() * 15 + 1);
		IDatabase.INSTANCE.setBalance(authorID, fluffies);

		final int energyCost = (int) (Math.random() * 8 + 1);
		IDatabase.INSTANCE.setAlpacaValues(authorID, "energy", -energyCost);

		final int joyCost = (int) (Math.random() * 10 + 2);
		IDatabase.INSTANCE.setAlpacaValues(authorID, "joy", -joyCost);

		final long cooldown = System.currentTimeMillis() + 1000L * 60 * 20;
		IDatabase.INSTANCE.setCooldown(authorID, "work", cooldown);

		final String message = ResourcesManager.getRandomMessage("work");

		channel.sendMessage("⛏ " + message + " **Fluffies + " + fluffies + ", Energy - " + energyCost + ", Joy - " + joyCost + "**").queue();
	}

	@Override
	public String getHelp(String prefix) {
		return "**Usage:** " + prefix + "work\n**Aliases:** " + getAliases() + "\n**Example:** " + prefix + "work";
	}

	@Override
	public String getName() {
		return "work";
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
