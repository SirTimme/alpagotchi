package bot.commands.interfaces;

import bot.utils.CommandType;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.Locale;

public interface ISlashCommand {
    CommandData getCommandData();

    CommandType getCommandType();
}
