package Bot.Command.Member;

import Bot.Command.ISlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.time.temporal.ChronoUnit;

public class Ping implements ISlashCommand {
    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        event.deferReply().queue();

        event.getHook().sendMessage("Pinging alpacafarm...").queue(msg -> {
            final long ping = ChronoUnit.MILLIS.between(event.getTimeCreated(), msg.getTimeCreated());
            event.getHook().editOriginal(":satellite: You reached the alpacafarm in **" + ping + "**ms").queue();
        });
    }
}
