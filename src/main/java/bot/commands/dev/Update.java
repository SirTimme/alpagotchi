package bot.commands.dev;

import bot.commands.CommandManager;
import bot.commands.InfoSlashCommand;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Locale;

public class Update extends InfoSlashCommand {
    private final CommandManager commandManager;

    public Update(final CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale) {
        event.getJDA()
             .updateCommands()
             .addCommands(this.commandManager.getCommandDataByTypes(CommandType.USER, CommandType.INFO, CommandType.INIT))
             .queue();

        event.getGuild()
             .updateCommands()
             .addCommands(this.commandManager.getCommandDataByTypes(CommandType.DEV))
             .queue();

        event.reply(Responses.getLocalizedResponse("updateCommands", locale, this.commandManager.getCommands().size())).queue();
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("update", "Refreshes all slash commands")
                       .setDescriptionLocalization(DiscordLocale.GERMAN, "Aktualisiert alle Befehle")
                       .setGuildOnly(true)
                       .setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.DEV;
    }
}