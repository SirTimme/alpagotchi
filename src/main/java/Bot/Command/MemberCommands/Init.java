package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.PermissionLevel;
import Bot.Config;
import Bot.Database.IDatabase;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.time.Instant;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ConstantConditions")
public class Init implements ICommand {
	private final EventWaiter waiter;
	private final String[] emoteIDs = {"782229268914372609", "782229279312314368"};

	public Init(EventWaiter waiter) {
		this.waiter = waiter;
	}

	@Override
	public void execute(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final long authorID = ctx.getAuthorID();
		final String prefix = ctx.getPrefix();

		if (IDatabase.INSTANCE.isUserInDB(authorID)) {
			channel.sendMessage("<:RedCross:782229279312314368> Your alpaca has already been set up").queue();
			return;
		}

		final User botCreator = ctx.getJDA().getUserById(Config.get("DEV_ID"));
		final EmbedBuilder embed = new EmbedBuilder();

		embed.setTitle("User information")
			 .setDescription("Im glad, that Alpagotchi interests you and you want to interact with him.\n" +
				 "Here are two important points before you can start:")
			 .setThumbnail(ctx.getJDA().getSelfUser().getAvatarUrl())
			 .addField(
				 "__§1 Storage of the UserID__",
				 "Alpagotchi stores your personal Discord UserID in order to work, " +
					 "but this is public information and can be accessed by everyone",
				 false
			 )
			 .addField(
				 "__§2 Deletion of the UserID__",
				 "If you change your mind about storing your UserID, " +
					 "use the `" + prefix + "delete` command to delete your data at any time",
				 false
			 )
			 .setImage("https://cdn.discordapp.com/attachments/795637300661977132/811504330263625778/Reactions.png")
			 .setFooter("Created by " + botCreator.getName(), botCreator.getEffectiveAvatarUrl())
			 .setTimestamp(Instant.now());

		channel.sendMessage(embed.build()).queue((msg) -> {
			msg.addReaction("GreenTick:" + emoteIDs[0]).queue();
			msg.addReaction("RedCross:" + emoteIDs[1]).queue();

			this.waiter.waitForEvent(
				GuildMessageReactionAddEvent.class,
				(event) -> event.getMessageIdLong() == msg.getIdLong()
					&& event.getMember().equals(ctx.getMember())
					&& event.getReactionEmote().isEmote()
					&& Arrays.asList(emoteIDs).contains(event.getReactionEmote().getEmote().getId()),
				(event) -> {
					final String emoteID = event.getReactionEmote().getEmote().getId();
					msg.delete().queue();

					if (emoteID.equals(emoteIDs[0])) {
						IDatabase.INSTANCE.createUserEntry(authorID);
						channel.sendMessage("<:GreenTick:782229268914372609> Your alpaca has been set up, " +
							"use **" + prefix + "myalpaca** to see it")
							   .queue();
					}
					else {
						channel.sendMessage("<:RedCross:782229279312314368> Init process cancelled").queue();
					}
				},
				90L, TimeUnit.SECONDS,
				() -> {
					msg.delete().queue();
					channel.sendMessage("<:RedCross:782229279312314368> Answer timed out").queue();
				}
			);
		});
	}

	@Override
	public String getHelp(String prefix) {
		return "**Usage:** " + prefix + "init\n" +
			"**Aliases:** " + getAliases() + "\n" +
			"**Example:** " + prefix + "init";
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

	@Override
	public EnumSet<Permission> getRequiredPermissions() {
		return EnumSet.of(
			Permission.MESSAGE_ADD_REACTION,
			Permission.MESSAGE_WRITE
		);
	}
}
