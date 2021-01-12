package Bot.Events;

import Bot.Command.CommandManager;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
    private final CommandManager cmdManager;

    public MessageListener() {
        this.cmdManager = new CommandManager();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        final String prefix = IDataBaseManager.INSTANCE.getPrefix(event.getGuild().getIdLong());

        if (event.getAuthor().isBot() || event.isWebhookMessage() || !event.getMessage().getContentRaw().startsWith(prefix)) {
            return;
        }

        cmdManager.handle(event, prefix);    }
}
