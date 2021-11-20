package bot.commands.interfaces;

import bot.utils.CommandType;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface IInfoCommand extends ISlashCommand {
    void execute(final SlashCommandEvent event);

    @Override
    default CommandType getCommandType() {
        return CommandType.INFO;
    }
}
