package bot.commands.interfaces;

import bot.models.Entry;
import bot.utils.CommandType;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.Locale;

public interface IStaticUserCommand extends ISlashCommand {
    void execute(final SlashCommandEvent event, final Entry user, final Locale locale);

    @Override
    default CommandType getCommandType(){
        return CommandType.STATIC_USER;
    }
}
