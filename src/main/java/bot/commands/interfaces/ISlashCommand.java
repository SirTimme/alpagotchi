package bot.commands.interfaces;

import bot.utils.CommandType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface ISlashCommand {
    CommandData getCommandData();

    CommandType getCommandType();
}
