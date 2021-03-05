package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.PermissionLevel;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.exceptions.PermissionException;

public class Pet implements ICommand {
	@Override
	public void execute(CommandContext ctx) throws PermissionException {
		if (!IDataBaseManager.INSTANCE.isUserInDB(ctx.getAuthorID())) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> You do not own a alpaca, use **" + ctx.getPrefix() + "init** first").queue();
			return;
		}

		final int joy = IDataBaseManager.INSTANCE.getAlpacaValues(ctx.getAuthorID(), "joy");
		if (joy == 100) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> The joy of your alpaca is already at the maximum").queue();
			return;
		}

		final int amountOfJoy = (int) (Math.random() * 10 + 1);
		final int newJoy = amountOfJoy + joy > 100 ? 100 - joy : amountOfJoy;
		IDataBaseManager.INSTANCE.setAlpacaValues(ctx.getAuthorID(), "joy", newJoy);

		ctx.getChannel().sendMessage("\uD83E\uDD99 Your alpaca loves to spend time with you **Joy + " + newJoy + "**").queue();
	}

	@Override
	public String getHelp(String prefix) {
		return "`Usage: " + prefix + "pet\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Pet your alpaca and increases his joy";
	}

	@Override
	public String getName() {
		return "pet";
	}

	@Override
	public Enum<PermissionLevel> getPermissionLevel() {
		return PermissionLevel.MEMBER;
	}
}