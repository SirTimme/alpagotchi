package Bot.Events;

import Bot.Command.CommandManager;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;

public class MessageListener extends ListenerAdapter {
	private final CommandManager cmdManager;
	private final static Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

	public MessageListener() {
		this.cmdManager = new CommandManager();
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		final String prefix = IDataBaseManager.INSTANCE.getPrefix(event.getGuild().getIdLong());

		if (event.getAuthor().isBot() || event.isWebhookMessage() || !event.getMessage().getContentRaw().startsWith(prefix) || !CheckPerms(event)) {
			return;
		}

		cmdManager.handle(event, prefix);
	}

	private boolean CheckPerms(GuildMessageReceivedEvent event)  {
		final Role botRole = event.getJDA().getRoleById(781068971759501343L);
		final Role everyoneRole = event.getJDA().getRoleById(769241891975069716L);

		if (botRole == null || everyoneRole == null) {
			LOGGER.error("Could not find the role");
			return false;
		}

		final EnumSet<Permission> channelDeniesBot = event.getChannel().getPermissionOverride(botRole) == null ? EnumSet.noneOf(Permission.class) : event.getChannel().getPermissionOverride(botRole).getDenied();
		final EnumSet<Permission> channelDeniesEveryone = event.getChannel().getPermissionOverride(everyoneRole) == null ? EnumSet.noneOf(Permission.class) : event.getChannel().getPermissionOverride(everyoneRole).getDenied();

		EnumSet<Permission> requiredPerms = EnumSet.of(
				Permission.MESSAGE_WRITE,
				Permission.MESSAGE_READ,
				Permission.MESSAGE_ATTACH_FILES,
				Permission.MESSAGE_ADD_REACTION,
				Permission.MESSAGE_HISTORY,
				Permission.MESSAGE_EMBED_LINKS,
				Permission.MESSAGE_EXT_EMOJI,
				Permission.VIEW_CHANNEL
		);

		for (Permission permission: requiredPerms) {
			if (channelDeniesBot.contains(permission) || channelDeniesEveryone.contains((permission))) {
				if (permission != Permission.MESSAGE_WRITE) {
					event.getChannel().sendMessage(":warning: I am missing at least the **" + permission.getName() + "** permission to work properly").queue();
				}
				return false;
			}
		}
		return true;
	}
}
