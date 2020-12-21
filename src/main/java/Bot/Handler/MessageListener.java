package Bot.Handler;

import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
    private final CommandManager manager;

    public MessageListener() {
        this.manager = new CommandManager();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        final User user = event.getAuthor();
        final long guildID = event.getGuild().getIdLong();
        final String prefix = IDataBaseManager.INSTANCE.getPrefix(guildID);
        final String rawMsg = event.getMessage().getContentRaw();

        if (user.isBot() || event.isWebhookMessage() || !rawMsg.startsWith(prefix)) {
            return;
        }

        manager.handle(event, prefix);
    }
}
