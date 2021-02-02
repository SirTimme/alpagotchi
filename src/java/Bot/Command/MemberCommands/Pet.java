package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Database.IDataBaseManager;

public class Pet implements ICommand {

	@Override
	public void execute(CommandContext commandContext) {
		if (!IDataBaseManager.INSTANCE.isUserInDB(commandContext.getAuthorID())) {
			commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> You do not own a alpaca, use **" + commandContext.getPrefix() + "init** first").queue();
			return;
		}

		final int joy = IDataBaseManager.INSTANCE.getAlpacaValues(commandContext.getAuthorID(), "joy");

		if (joy == 100) {
			commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> The joy of your alpaca is already at the maximum").queue();
			return;
		}

		int amountOfJoy = (int) (Math.random() * 10 + 1);
		int newJoy = amountOfJoy + joy > 100 ? 100 - joy : amountOfJoy;

		IDataBaseManager.INSTANCE.setAlpacaValues(commandContext.getAuthorID(), "joy", newJoy);

		commandContext.getChannel().sendMessage("\uD83E\uDD99 Your alpaca loves to spend time with you **Joy + " + newJoy + "**").queue();
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