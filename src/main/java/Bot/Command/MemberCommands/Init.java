package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.PermissionLevel;
import Bot.Config;
import Bot.Database.IDataBaseManager;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;

import java.time.Instant;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ConstantConditions")
public class Init implements ICommand {
	private final EventWaiter waiter;
	private final String[] acceptedEmotes = {"<:GreenTick:782229268914372609>", "<:RedCross:782229279312314368>"};
	private final EnumSet<Permission> permissions = EnumSet.of(Permission.MESSAGE_MANAGE, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_HISTORY);

	public Init(EventWaiter waiter) {
		this.waiter = waiter;
	}

	@Override
	public void execute(CommandContext ctx) throws PermissionException {
		if (IDataBaseManager.INSTANCE.isUserInDB(ctx.getAuthorID())) {
			ctx.getChannel().sendMessage("<:RedCross:782229279312314368> Your alpaca has already been set up").queue();
			return;
		}

		permissions.forEach(permission -> {
			if (!ctx.getGuild().getSelfMember().hasPermission(ctx.getChannel(), permission)) {
				throw new PermissionException(permission.getName());
			}
		});

		final User botCreator = ctx.getJDA().getUserById(Config.get("OWNER_ID"));
		final EmbedBuilder embed = new EmbedBuilder();
		embed
				.setTitle("User information")
				.setDescription("Im glad, that Alpagotchi interests you and you want to interact with him.\nHere are two important points before you can start:")
				.setThumbnail(ctx.getJDA().getSelfUser().getAvatarUrl())
				.addField(
						"__§1 Storage of the UserID__",
						"Alpagotchi stores your personal Discord UserID in order to work, but this is public information and can be accessed by everyone",
						false
				)
				.addField(
						"__§2 Deletion of the UserID__",
						"If you change your mind about storing your UserID, use the `" + ctx.getPrefix() + "delete` command to delete your data at any time",
						false
				)
				.setImage("https://cdn.discordapp.com/attachments/795637300661977132/811504330263625778/Reactions.png")
				.setFooter("Created by " + botCreator.getName(), botCreator.getEffectiveAvatarUrl())
				.setTimestamp(Instant.now());

		ctx.getChannel().sendMessage(embed.build()).queue((message) -> {
			message.addReaction(acceptedEmotes[0]).queue();
			message.addReaction(acceptedEmotes[1]).queue();

			this.waiter.waitForEvent(
					GuildMessageReactionAddEvent.class,
					(event) -> event.getMessageIdLong() == message.getIdLong()
							&& event.getMember().equals(ctx.getMember())
							&& !event.getReactionEmote().isEmote()
							&& Arrays.asList(acceptedEmotes).contains(event.getReactionEmote().getEmoji()),
					(event) -> {
						message.suppressEmbeds(true).queue();
						if (event.getReactionEmote().getEmoji().equals(acceptedEmotes[0])) {
							IDataBaseManager.INSTANCE.createDBEntry(ctx.getAuthorID());
							message.editMessage("<:GreenTick:782229268914372609> Your alpaca has been set up, use **" + ctx.getPrefix() + "myalpaca** to see it").queue();
						} else if (event.getReactionEmote().getEmoji().equals(acceptedEmotes[1])) {
							message.editMessage("<:RedCross:782229279312314368> Initiation process cancelled").queue();
						}
						message.clearReactions().queue();
					},
					90L, TimeUnit.SECONDS,
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
