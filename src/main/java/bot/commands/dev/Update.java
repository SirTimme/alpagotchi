package bot.commands.dev;

import bot.commands.CommandManager;
import bot.commands.InfoCommand;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.text.MessageFormat;
import java.util.*;

public class Update extends InfoCommand {
    private final CommandManager commands;

    public Update(final CommandManager commands) {
        this.commands = commands;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale) {
        event.getJDA()
             .updateCommands()
             .addCommands(this.commands.getCommandDataByTypes(CommandType.USER, CommandType.INFO, CommandType.INIT))
             .queue();

        event.getGuild()
             .updateCommands()
             .addCommands(this.commands.getCommandDataByTypes(CommandType.DEV))
             .queue();

        final var format = new MessageFormat(Responses.get("update", locale));
        final var msg = format.format(new Object[]{ this.commands.getCommands().size() });

        event.reply(msg).queue();
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("update", "Refreshes all slashcommands")
                       .setGuildOnly(true)
                       .setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.DEV;
    }
}