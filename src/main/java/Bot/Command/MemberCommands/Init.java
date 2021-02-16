package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Config;
import Bot.Database.IDataBaseManager;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Init implements ICommand {
	private final EventWaiter waiter;
	private static final String acceptEmote = "✅";
	private static final String declineEmote = "❌";

	public Init(EventWaiter waiter) {
		this.waiter = waiter;
	}

	@Override
	public void execute(CommandContext ctx) {

		if (IDataBaseManager.INSTANCE.isUserInDB(ctx.getAuthorID())) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> Your alpaca has already been set up").queue();
			return;
		}

		final User botCreator = ctx.getJDA().getUserById(Config.get("OWNER_ID"));

		EmbedBuilder embed = new EmbedBuilder();
		embed
				.setTitle("User information")
				.setDescription("Im glad, that Alpagotchi interests you and you want to interact with him. Here are two important points before you can start:")
				.setThumbnail(ctx.getJDA().getSelfUser().getAvatarUrl())
				.addField("__§1 Personal data storage__", "Alpagotchi stores your personal Discord UserID in order to work\n\n" +
						"React with the " + acceptEmote + " when you give Alpagotchi the permission to store this information and let's start this journey!\n\n" +
						"React with the " + declineEmote + " when you think it's too big a risk for Alpagotchi to store this information and our journey ends here", false)
				.addField("__§2 Deletion of personal data__", "If you change your mind about storing your UserID, use the `" + ctx.getPrefix() + "delete` command to delete your data at any time", false)
				.setFooter("Created by " + botCreator.getName(), botCreator.getEffectiveAvatarUrl())
				.setTimestamp(Instant.now());

		ctx.getChannel().sendMessage(embed.build())
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
									IDataBaseManager.INSTANCE.createDBEntry(ctx.getAuthorID());
									message.suppressEmbeds(true).queue();
									message.editMessage("<:GreenTick:782229268914372609> Your alpaca has been set up").queue();
								} else if (event.getReactionEmote().getEmoji().equals(declineEmote)) {
									message.suppressEmbeds(true).queue();
									message.editMessage("<:RedCross:782229279312314368> Initiation process cancelled").queue();
								}
								message.clearReactions().queue();
							},
							1L, TimeUnit.MINUTES,
							() -> {
								message.suppressEmbeds(true).queue();
								message.editMessage("<:RedCross:782229279312314368> Answer timed out").queue();
								message.clearReactions().queue();
							}
					);
				});
	}

	@Override
	public String getHelp(String prefix) {
		return "`Usage: " + prefix + "init\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Initialize your alpaca in the database";
	}

	@Override
	public String getName() {
		return "init";
	}

	@Override
	public Enum<PermissionLevel> getPermissionLevel() {
		return PermissionLevel.MEMBER;
	}

	@Override
	public List<String> getAliases() {
		return List.of("setup");
	}
}
