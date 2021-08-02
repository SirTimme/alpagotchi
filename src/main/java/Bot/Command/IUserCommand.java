package Bot.Command;

import Bot.Models.DBUser;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface IUserCommand extends ISlashCommand {
    void execute(SlashCommandEvent event, DBUser user);
}
