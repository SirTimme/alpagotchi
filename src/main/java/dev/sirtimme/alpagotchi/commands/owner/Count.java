package dev.sirtimme.alpagotchi.commands.owner;

import dev.sirtimme.alpagotchi.commands.types.OwnerCommand;
import dev.sirtimme.alpagotchi.db.IDatabase;
import dev.sirtimme.alpagotchi.commands.types.CommandType;
import dev.sirtimme.alpagotchi.localization.LocalizedResponse;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Locale;

public class Count extends OwnerCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale) {
        final var userCount = IDatabase.INSTANCE.getUserCount();
        final var guildSize = event.getJDA().getGuilds().size();

        event.reply(LocalizedResponse.get("count.alpacas", locale, userCount, guildSize)).queue();
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("count", "Counts all alpacas of Alpagotchi")
                       .setDescriptionLocalization(DiscordLocale.GERMAN, "Zählt alle Alpakas von Alpagotchi")
                       .setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.OWNER;
    }
}