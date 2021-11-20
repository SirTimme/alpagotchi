package bot.commands.interfaces;

import bot.models.Entry;
import bot.utils.CommandType;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface IStaticUserCommand extends ISlashCommand {
    void execute(final SlashCommandEvent event, final Entry user);

    @Override
    default CommandType getCommandType(){
        return CommandType.STATIC_USER;
    }
}
