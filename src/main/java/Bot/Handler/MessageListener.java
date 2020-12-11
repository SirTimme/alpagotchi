package Bot.Handler;

import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import Bot.Config;

public class MessageListener extends ListenerAdapter {
    private final CommandManager manager = new CommandManager();

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        final User user = event.getAuthor();
        final long guildID = event.getGuild().getIdLong();
        final String prefix = IDataBaseManager.INSTANCE.getPrefix(guildID);
        final String rawMsg = event.getMessage().getContentRaw();

        if (user.isBot() || event.isWebhookMessage() || !rawMsg.startsWith(prefix)) {
            return;
        }

        if (rawMsg.equalsIgnoreCase(prefix + "shutdown") && user.getId().equals(Config.get("OWNER_ID"))) {
            event.getChannel().sendMessage("Alpagotchi is shutting down...").complete();
            System.exit(0);
        }

        manager.handle(event, prefix);
    }
}
