package Bot.Command.Member;

import Bot.Command.ISlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class Ping implements ISlashCommand {
    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        event.getJDA()
             .getRestPing()
             .queue((ping) -> event.reply(":satellite: You reached the alpacafarm in **" + ping + "**ms").queue());
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("ping", "Displays the current latency of Alpagotchi");
    }
}
