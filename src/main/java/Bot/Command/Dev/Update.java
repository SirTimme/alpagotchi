package Bot.Command.Dev;

import Bot.Command.IInfoCommand;
import Bot.Command.SlashCommandManager;
import Bot.Config;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static Bot.Utils.Emote.GREENTICK;
import static Bot.Utils.Emote.REDCROSS;

public class Update implements IInfoCommand {
    private final SlashCommandManager slashCmdMan;
    private final static Set<String> DEV_COMMANDS = Set.of("decrease", "shutdown", "update", "count");

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

        slashCmdMan.getCommands().values().forEach(cmd -> {
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
                guild.updateCommandPrivilegesById(cmd.getIdLong(), CommandPrivilege.enableUser(Config.get("DEV_ID"))).queue();
            }
        }
        event.reply(GREENTICK + " Successfully refreshed all slash commands").queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("update", "Refreshes all slashcommands").setDefaultEnabled(false);
    }
}
