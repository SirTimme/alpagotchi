package Bot;

import Bot.Database.IDataBaseManager;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
    private final CommandManager manager = new CommandManager();

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        User user = event.getAuthor();

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        final long guildID = event.getGuild().getIdLong();
        String prefix = Constants.PREFIXES.computeIfAbsent(guildID, IDataBaseManager.INSTANCE::getPrefix);
        String rawMsg = event.getMessage().getContentRaw();

        if (rawMsg.equalsIgnoreCase(prefix + "shutdown") && user.getId().equals(Config.get("OWNER_ID"))) {
            event.getChannel().sendMessage("Alpagotchi is shutting down...").queue();

            event.getJDA().shutdown();
            BotCommons.shutdown(event.getJDA());

            return;
        }

        if (rawMsg.startsWith(prefix)) {
            manager.handle(event, prefix);
        }
    }
}
