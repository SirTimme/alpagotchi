package bot.commands;

import bot.models.Entry;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface IStaticUserCommand extends ISlashCommand {
    void execute(final SlashCommandEvent event, final Entry user);
}
