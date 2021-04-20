package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.Error;
import Bot.Utils.Language;
import Bot.Utils.PermLevel;
import Bot.Database.IDatabase;
import Bot.Utils.Stat;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.EnumSet;
import java.util.List;

public class Balance implements ICommand {
	@Override
	public void execute(CommandContext ctx) {
		final long authorID = ctx.getAuthorID();
		final TextChannel channel = ctx.getChannel();

		if (IDatabase.INSTANCE.getUser(authorID) == null) {
			channel.sendMessage(Error.NOT_INITIALIZED.getMessage(ctx.getPrefix(), getName())).queue();
			return;
		}

		final int balance = IDatabase.INSTANCE.getStatInt(authorID, Stat.CURRENCY);

		channel.sendMessage("\uD83D\uDCB5 Your current balance is **" + Language.handle(balance, "fluffy") + "**").queue();
	}

	@Override
	public String getName() {
		return "balance";
	}

	@Override
	public PermLevel getPermLevel() {
		return PermLevel.MEMBER;
	}

	@Override
	public List<String> getAliases() {
		return List.of("wallet", "money", "fluffies");
	}

	@Override
	public EnumSet<Permission> getCommandPerms() {
		return EnumSet.of(Permission.MESSAGE_WRITE);
	}

	@Override
	public String getSyntax() {
		return "balance";
	}

	@Override
	public String getDescription() {
		return "Displays your balance of fluffies";
	}
}
