package Bot.Command.Dev;

import Bot.Command.ISlashCommand;
import Bot.Command.SlashCommandManager;
import Bot.Config;
import Bot.Utils.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;

import java.util.List;
import java.util.Set;

public class Update implements ISlashCommand {
    private final SlashCommandManager slashCmdMan;
    private final static Set<String> DEV_COMMANDS = Set.of("decrease", "shutdown", "update");

    public Update(SlashCommandManager slashCmdMan) {
        this.slashCmdMan = slashCmdMan;
    }

    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        final Guild guild = event.getGuild();
        final List<Command> commands = guild.retrieveCommands().complete();

        for (Command cmd : commands) {
            if (DEV_COMMANDS.contains(cmd.getName())) {
                guild.updateCommandPrivilegesById(cmd.getIdLong(), CommandPrivilege.enableUser(Config.get("DEV_ID")))
                     .queue();
            }
        }

        slashCmdMan.getCommands().values().forEach(cmd -> {
            final CommandData cmdData = cmd.getCommandData();
            if (DEV_COMMANDS.contains(cmdData.getName())) {
                guild.upsertCommand(cmdData).queue();
            } else {
                event.getJDA().upsertCommand(cmdData).queue();
            }
        });

        event.reply(Emote.GREENTICK + " Successfully refreshed all slash commands").queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("update", "Refreshes all slashcommands").setDefaultEnabled(false);
    }
}
