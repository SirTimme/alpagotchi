package bot.commands.dev;

import bot.commands.InfoSlashCommand;
import bot.db.IDatabase;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Locale;

public class Count extends InfoSlashCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale) {
        final var userCount = IDatabase.INSTANCE.getUserCount();
        final var guildSize = event.getJDA().getGuilds().size();

        event.reply(Responses.getLocalizedResponse("count.alpacas", locale, userCount, guildSize)).queue();
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("count", "Counts all alpacas of Alpagotchi")
                       .setDescriptionLocalization(DiscordLocale.GERMAN, "Zählt alle Alpakas von Alpagotchi")
                       .setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.DEV;
    }
}