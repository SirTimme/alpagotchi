package Bot.Events;

import Bot.Command.CommandManager;
import Bot.Database.IDataBaseManager;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
	private final CommandManager cmdManager;

	public MessageListener(EventWaiter waiter) {
		this.cmdManager = new CommandManager(waiter);
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		final String prefix = IDataBaseManager.INSTANCE.getPrefix(event.getGuild().getIdLong());

		if (!event.getMessage().getContentRaw().startsWith(prefix) || event.getAuthor().isBot() || event.isWebhookMessage()) {
			return;
		}

		this.cmdManager.handle(event, prefix);
	}
}
