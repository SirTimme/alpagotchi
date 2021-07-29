package Bot.Command;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface ISlashCommand {
    void execute(SlashCommandEvent event, long authorID);

    CommandData getCommandData();
}
