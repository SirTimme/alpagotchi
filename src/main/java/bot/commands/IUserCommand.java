package bot.commands;

import bot.models.Entry;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface IUserCommand extends ISlashCommand {
    Entry execute(SlashCommandEvent event, Entry user);
}
