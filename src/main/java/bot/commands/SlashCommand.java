package bot.commands;

import bot.models.Entry;
import bot.utils.CommandType;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.Locale;

public abstract class SlashCommand {
    protected abstract CommandData getCommandData();

    protected abstract CommandType getCommandType();

    protected abstract void execute(final SlashCommandEvent event, final Locale locale, final Entry user);
}
