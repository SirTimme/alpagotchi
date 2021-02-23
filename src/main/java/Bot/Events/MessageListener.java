package Bot.Events;

import Bot.Command.CommandManager;
import Bot.Config;
import Bot.Database.IDataBaseManager;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.EnumSet;

@SuppressWarnings("ConstantConditions")
public class MessageListener extends ListenerAdapter {
	private final CommandManager cmdManager;

	public MessageListener(EventWaiter waiter) {
		this.cmdManager = new CommandManager(waiter);
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		final String prefix = IDataBaseManager.INSTANCE.getPrefix(event.getGuild().getIdLong());

		if (!event.getMessage().getContentRaw().startsWith(prefix) || event.getAuthor().isBot() || !checkPermissions(event) || event.isWebhookMessage()) {
			return;
		}

		this.cmdManager.handle(event, prefix);
	}

	private boolean checkPermissions(GuildMessageReceivedEvent event) {
		final Member botClient = event.getGuild().getMemberById(event.getJDA().getSelfUser().getIdLong());
		EnumSet<Permission> botPermissions = botClient.getPermissions(event.getChannel());

		for (Permission permission : Config.requiredPermissions()) {
			if (!botPermissions.contains(permission)) {
				if (permission != Permission.MESSAGE_WRITE) {
					event.getChannel().sendMessage("⚠ I am missing at least the **" + permission.getName() + "** permission to work properly").queue();
				}
				return false;
			}
		}
		return true;
	}
}
