package bot.commands;

import bot.models.DBUser;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface IUserCommand extends ISlashCommand {
    void execute(SlashCommandEvent event, DBUser user);
}
