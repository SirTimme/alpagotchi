package bot.commands.interfaces;

import bot.utils.CommandType;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.Locale;

public interface IInfoCommand extends ISlashCommand {
    void execute(final SlashCommandEvent event, final Locale locale);

    @Override
    default CommandType getCommandType() {
        return CommandType.INFO;
    }
}
