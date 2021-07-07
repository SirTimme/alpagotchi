package Bot.Command.Member;

import Bot.Command.ISlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class Ping implements ISlashCommand {
    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        event.getJDA()
             .getRestPing()
             .queue((ping) -> event.reply(":satellite: You reached the alpacafarm in **" + ping + "**ms").queue());
    }
}
