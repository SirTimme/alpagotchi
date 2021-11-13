package bot.commands.dev;

import bot.commands.IInfoCommand;
import bot.commands.SlashCommandManager;
import bot.utils.Env;
import bot.utils.Resources;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static bot.utils.Emote.REDCROSS;

public class Update implements IInfoCommand {
    private final SlashCommandManager slashCmdMan;
    private final static Set<String> DEV_COMMANDS = Set.of("shutdown", "update", "count");

    public Update(SlashCommandManager slashCmdMan) {
        this.slashCmdMan = slashCmdMan;
    }

    @Override
    public void execute(SlashCommandEvent event) {
        final Guild guild = event.getGuild();
        if (guild == null) {
            event.reply(REDCROSS + " You need to execute this command in a guild!")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        slashCmdMan.getCommands().forEach(cmd -> {
            final CommandData cmdData = cmd.getCommandData();

            if (DEV_COMMANDS.contains(cmdData.getName())) {
                guild.upsertCommand(cmdData).queue();
            } else {
                event.getJDA().upsertCommand(cmdData).queue();
            }
        });

        List<Command> commands = new ArrayList<>();
        guild.retrieveCommands().queue(commands::addAll);

        for (Command cmd : commands) {
            if (DEV_COMMANDS.contains(cmd.getName())) {
                guild.updateCommandPrivilegesById(cmd.getIdLong(), CommandPrivilege.enableUser(Env.get("DEV_ID"))).queue();
            }
        }

        final MessageFormat formatter = new MessageFormat(Resources.getPattern("update"));
        event.reply(formatter.format(new Object())).queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("update", "Refreshes all slashcommands").setDefaultEnabled(false);
    }
}
