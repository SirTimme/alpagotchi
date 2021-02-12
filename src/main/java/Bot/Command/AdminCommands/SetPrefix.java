package Bot.Command.AdminCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.Permission;

import java.util.List;

public class SetPrefix implements ICommand {

	@Override
	public void execute(CommandContext commandContext) {

		if (!PermissionLevel.ADMIN.hasPerms(commandContext.getMember())) {
			commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> This is a **admin-only** command, you are missing the **" + Permission.MANAGE_SERVER.getName() + "** permission").queue();
			return;
		}

		final List<String> args = commandContext.getArgs();

		if (args.isEmpty()) {
			commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Missing arguments").queue();
			return;
		}

		String newPrefix = String.join("", args);
		IDataBaseManager.INSTANCE.setPrefix(commandContext.getGuild().getIdLong(), newPrefix);

		commandContext.getChannel().sendMessage("<:GreenTick:782229268914372609> The prefix of **" + commandContext.getGuild().getName() + "** has been set to **" + newPrefix + "**").queue();
	}

	@Override
	public String getHelp(String prefix) {
		return "`Usage: " + prefix + "setprefix [prefix]\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Sets the prefix for this server";
	}

	@Override
	public String getName() {
		return "setprefix";
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.ADMIN;
	}
}
