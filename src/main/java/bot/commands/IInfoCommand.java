package bot.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface IInfoCommand extends ISlashCommand {
    void execute(final SlashCommandEvent event);
}
