package Bot.Command.Member;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.Emote;
import Bot.Utils.Error;
import Bot.Database.IDatabase;
import Bot.Utils.Level;
import Bot.Utils.Stat;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.EnumSet;
import java.util.List;

public class Nick implements ICommand {
	@Override
	public void execute(CommandContext ctx) {
		final long authorID = ctx.getAuthorID();
		final TextChannel channel = ctx.getChannel();
		final List<String> args = ctx.getArgs();

		if (IDatabase.INSTANCE.getUser(authorID) == null) {
			channel.sendMessage(Error.NOT_INITIALIZED.getMessage(ctx.getPrefix(), getName())).queue();
			return;
		}

		if (args.isEmpty()) {
			channel.sendMessage(Emote.REDCROSS + " Please specify the nickname").queue();
			return;
		}

		final String nickname = String.join(" ", args);
		if (nickname.length() > 256) {
			channel.sendMessage(Emote.REDCROSS + " The nickname mustn't exceed **256** characters").queue();
			return;
		}

		IDatabase.INSTANCE.setStatString(authorID, Stat.NICKNAME, nickname);

		channel.sendMessage("\uD83D\uDD8A The nickname of your alpaca has been set to **" + nickname + "**").queue();
	}

	@Override
	public String getName() {
		return "nick";
	}

	@Override
	public Level getLevel() {
		return Level.MEMBER;
	}

	@Override
	public EnumSet<Permission> getCommandPerms() {
		return EnumSet.of(Permission.MESSAGE_WRITE);
	}

	@Override
	public String getSyntax() {
		return "nick [nickname]";
	}

	@Override
	public String getExample() {
		return "nick Fluffy";
	}

	@Override
	public String getDescription() {
		return "Gives your alpaca a nickname";
	}
}