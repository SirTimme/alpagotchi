package Bot.Command.Member;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.Emote;
import Bot.Utils.Error;
import Bot.Database.IDatabase;
import Bot.Utils.Level;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

public class Delete implements ICommand {
	private final EventWaiter waiter;
	private final String[] emoteIDs = {"782229268914372609", "782229279312314368"};

	public Delete(EventWaiter waiter) {
		this.waiter = waiter;
	}

	@Override
	public void execute(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final long authorID = ctx.getAuthorID();

		if (IDatabase.INSTANCE.getUser(authorID) == null) {
			channel.sendMessage(Error.NOT_INITIALIZED.getMessage(ctx.getPrefix(), getName())).queue();
			return;
		}

		channel.sendMessage("⚠ Are you sure you want to delete your data? You **permanently** lose all progress").queue((msg) -> {
			msg.addReaction("GreenTick:" + emoteIDs[0]).queue();
			msg.addReaction("RedCross:" + emoteIDs[1]).queue();

			waiter.waitForEvent(
				GuildMessageReactionAddEvent.class,
				(event) -> event.getMessageIdLong() == msg.getIdLong()
					&& event.getMember().equals(ctx.getMember())
					&& event.getReactionEmote().isEmote()
					&& Arrays.asList(emoteIDs).contains(event.getReactionEmote().getEmote().getId()),
				(event) -> {
					final String emoteID = event.getReactionEmote().getEmote().getId();
					msg.delete().queue();

					if (emoteID.equals(emoteIDs[0])) {
						IDatabase.INSTANCE.deleteUser(authorID);
						channel.sendMessage(Emote.GREENTICK + " Data successfully deleted").queue();
					}
					else {
						channel.sendMessage(Emote.REDCROSS + " Delete process cancelled").queue();
					}
				},
				90L, TimeUnit.SECONDS,
				() -> {
					msg.delete().queue();
					channel.sendMessage(Emote.REDCROSS + " Answer timed out").queue();
				}
			);
		});
	}

	@Override
	public String getName() {
		return "delete";
	}

	@Override
	public Level getLevel() {
		return Level.MEMBER;
	}

	@Override
	public EnumSet<Permission> getCommandPerms() {
		return EnumSet.of(
			Permission.MESSAGE_WRITE,
			Permission.MESSAGE_ADD_REACTION
		);
	}

	@Override
	public String getSyntax() {
		return "delete";
	}

	@Override
	public String getDescription() {
		return "Deletes all of your data";
	}
}
