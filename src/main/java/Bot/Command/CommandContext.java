package Bot.Command;

import me.duncte123.botcommons.commands.ICommandContext;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class CommandContext implements ICommandContext {
    private final GuildMessageReceivedEvent event;
    private final List<String> args;
    private final long authorID;
    private final String prefix;

    public CommandContext(GuildMessageReceivedEvent event, List<String> args, long authorID, String prefix) {
        this.event = event;
        this.args = args;
        this.authorID = authorID;
        this.prefix = prefix;
    }

    @Override
    public Guild getGuild() {
        return this.getEvent().getGuild();
    }

    @Override
    public GuildMessageReceivedEvent getEvent() {
        return this.event;
    }

    public List<String> getArgs() {
        return this.args;
    }

    public long getAuthorID() {
        return this.authorID;
    }

    public String getPrefix() {
        return this.prefix;
    }
}
