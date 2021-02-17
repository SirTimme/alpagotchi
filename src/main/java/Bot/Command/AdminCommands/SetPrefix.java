package Bot.Command.AdminCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.Permission;

public class SetPrefix implements ICommand {
	@Override
	public void execute(CommandContext ctx) {
		if (!PermissionLevel.ADMIN.hasPerms(ctx.getMember())) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> This is a **admin-only** command, you are missing the **" + Permission.MANAGE_SERVER.getName() + "** permission").queue();
			return;
		}

		if (ctx.getArgs().isEmpty()) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> Could not resolve the new prefix").queue();
			return;
		}

		String newPrefix = String.join("", ctx.getArgs());
		IDataBaseManager.INSTANCE.setPrefix(ctx.getGuild().getIdLong(), newPrefix);

		ctx.getChannel().sendMessage("<:GreenTick:782229268914372609> The prefix of **" + ctx.getGuild().getName() + "** has been set to **" + newPrefix + "**").queue();
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
