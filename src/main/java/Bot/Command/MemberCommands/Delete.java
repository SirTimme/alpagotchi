package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Database.IDataBaseManager;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.concurrent.TimeUnit;

public class Delete implements ICommand {
	private final EventWaiter waiter;
	private static final String acceptEmote = "✅";
	private static final String declineEmote = "❌";

	public Delete(EventWaiter waiter) {
		this.waiter = waiter;
	}

	@Override
	public void execute(CommandContext ctx) {
		if (!IDataBaseManager.INSTANCE.isUserInDB(ctx.getAuthorID())) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> You don't own an alpaca, so Alpagotchi stores no data about you").queue();
			return;
		}

		ctx.getChannel().sendMessage("⚠ Are you sure you want to delete your data? You **permanently** lose all progress")
				.queue((message) -> {
					message.addReaction(acceptEmote).queue();
					message.addReaction(declineEmote).queue();

					this.waiter.waitForEvent(
							GuildMessageReactionAddEvent.class,
							(event) -> event.getMessageIdLong() == message.getIdLong() && !event.getUser().isBot() && event.getMember().equals(ctx.getMember()),
							(event) -> {
								if (event.getReactionEmote().isEmote() || !(event.getReactionEmote().getEmoji().equals(acceptEmote) || event.getReactionEmote().getEmoji().equals(declineEmote))) {
									message.clearReactions().queue();
									message.editMessage("<:RedCross:782229279312314368> Invalid reaction").queue();
									return;
								}
								if (event.getReactionEmote().getEmoji().equals(acceptEmote)) {
									IDataBaseManager.INSTANCE.deleteDBEntry(ctx.getAuthorID());
									message.editMessage("<:GreenTick:782229268914372609> Personal data successfully deleted").queue();
								} else if (event.getReactionEmote().getEmoji().equals(declineEmote)) {
									message.editMessage("<:RedCross:782229279312314368> Delete process cancelled").queue();
								}
								message.clearReactions().queue();
							},
							1L, TimeUnit.MINUTES,
							() -> {
								message.clearReactions().queue();
								message.editMessage("<:RedCross:782229279312314368> Answer timed out").queue();
							}
					);
				});
	}

	@Override
	public String getHelp(String prefix) {
		return "`Usage: " + prefix + "delete\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Deletes all of your stored data";
	}

	@Override
	public String getName() {
		return "delete";
	}

	@Override
	public Enum<PermissionLevel> getPermissionLevel() {
		return PermissionLevel.MEMBER;
	}
}
