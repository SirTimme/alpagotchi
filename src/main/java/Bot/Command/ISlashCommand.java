package Bot.Command;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface ISlashCommand {
    void execute(SlashCommandEvent event, long authorID);
}
