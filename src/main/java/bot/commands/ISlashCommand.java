package bot.commands;

import bot.commands.types.CommandType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface ISlashCommand {
    void execute(final SlashCommandInteractionEvent event);

    CommandData getCommandData();

    CommandType getCommandType();
}