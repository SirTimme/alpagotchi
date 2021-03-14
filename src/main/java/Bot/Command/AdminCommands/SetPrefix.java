package Bot.Command.AdminCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.PermissionLevel;
import Bot.Database.IDatabase;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.EnumSet;
import java.util.List;

public class SetPrefix implements ICommand {
	@Override
	public void execute(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final List<String> args = ctx.getArgs();
		final Guild guild = ctx.getGuild();

		if (!PermissionLevel.ADMIN.hasPermission(ctx.getMember())) {
			channel.sendMessage("<:RedCross:782229279312314368> This is an **admin-only** command, you're missing the **" + Permission.MANAGE_SERVER.getName() + "** permission").queue();
			return;
		}

		if (args.isEmpty()) {
			channel.sendMessage("<:RedCross:782229279312314368> Couldn't resolve the new prefix").queue();
			return;
		}

		final String newPrefix = String.join("", args);
		IDatabase.INSTANCE.setPrefix(guild.getIdLong(), newPrefix);

		ctx.getChannel().sendMessage("<:GreenTick:782229268914372609> The prefix of **" + guild.getName() + "** has been set to **" + newPrefix + "**").queue();
	}

	@Override
	public String getHelp(String prefix) {
		return "**" + prefix + "setprefix [prefix]**\n*+Aliases:** " + getAliases() + "\n**Example:** " + prefix + "setprefix ?";
	}

	@Override
	public String getName() {
		return "setprefix";
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.ADMIN;
	}

	@Override
	public EnumSet<Permission> getRequiredPermissions() {
		return EnumSet.of(Permission.MESSAGE_WRITE);
	}
}
