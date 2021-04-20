package Bot.Events;

import Bot.Command.CommandManager;
import Bot.Database.IDatabase;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.UnavailableGuildJoinedEvent;
import net.dv8tion.jda.api.events.guild.UnavailableGuildLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventHandler extends ListenerAdapter {
	private final CommandManager commandManager;

	public EventHandler(EventWaiter waiter) {
		this.commandManager = new CommandManager(waiter);
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		final String prefix = IDatabase.INSTANCE.getPrefix(event.getGuild().getIdLong());
		final String msg = event.getMessage().getContentRaw();

		if (!msg.startsWith(prefix) || event.getAuthor().isBot() || event.isWebhookMessage()) {
			return;
		}

		commandManager.handle(event, prefix);
	}

	@Override
	public void onGuildJoin(@NotNull GuildJoinEvent event) {
		IDatabase.INSTANCE.createGuild(event.getGuild().getIdLong());
	}

	@Override
	public void onUnavailableGuildJoined(UnavailableGuildJoinedEvent event) {
		IDatabase.INSTANCE.createGuild(event.getGuildIdLong());
	}

	@Override
	public void onGuildLeave(@NotNull GuildLeaveEvent event) {
		IDatabase.INSTANCE.deleteGuild(event.getGuild().getIdLong());
	}

	@Override
	public void onUnavailableGuildLeave(@NotNull UnavailableGuildLeaveEvent event) {
		IDatabase.INSTANCE.deleteGuild(event.getGuildIdLong());
	}
}
