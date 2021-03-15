package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.PermissionLevel;
import Bot.Database.IDatabase;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.EnumSet;

public class Pet implements ICommand {
	@Override
	public void execute(CommandContext ctx) {
		final long authorID = ctx.getAuthorID();
		final TextChannel channel = ctx.getChannel();

		if (!IDatabase.INSTANCE.isUserInDB(authorID)) {
			channel.sendMessage("<:RedCross:782229279312314368> You don't own an alpaca, " +
				"use **" + ctx.getPrefix() + "init** first")
				   .queue();
			return;
		}

		final int joy = IDatabase.INSTANCE.getAlpacaValues(authorID, "joy");
		if (joy == 100) {
			channel.sendMessage("<:RedCross:782229279312314368> The joy of your alpaca is already at the maximum")
				   .queue();
			return;
		}

		final int amountOfJoy = (int) (Math.random() * 10 + 1);
		final int newJoy = amountOfJoy + joy > 100
						   ? 100 - joy
						   : amountOfJoy;

		IDatabase.INSTANCE.setAlpacaValues(authorID, "joy", newJoy);

		channel.sendMessage("\uD83E\uDD99 Your alpaca loves to spend time with you **Joy + " + newJoy + "**").queue();
	}

	@Override
	public String getHelp(String prefix) {
		return "**Usage:** " + prefix + "pet\n" +
			"**Aliases:** " + getAliases() + "\n" +
			"**Example:** " + prefix + "pet";
	}

	@Override
	public String getName() {
		return "pet";
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