package bot.commands;

import bot.models.Entry;
import bot.utils.CommandType;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.Locale;

public interface ISlashCommand {
    CommandData getCommandData();

    CommandType getCommandType();

    void execute(final SlashCommandEvent event, final Locale locale, final Entry user);
}
