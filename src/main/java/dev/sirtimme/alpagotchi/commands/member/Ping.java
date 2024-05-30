package dev.sirtimme.alpagotchi.commands.member;

import dev.sirtimme.alpagotchi.commands.types.InfoCommand;
import dev.sirtimme.alpagotchi.commands.types.CommandType;
import dev.sirtimme.alpagotchi.localization.LocalizedResponse;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Locale;

public class Ping extends InfoCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale) {
        event.getJDA().getRestPing().queue(ping -> event.reply(LocalizedResponse.get("ping.successful", locale, ping)).queue());
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("ping", "Displays the current latency of Alpagotchi")
                       .setDescriptionLocalization(DiscordLocale.GERMAN, "Zeigt die aktuelle Latenz von Alpagotchi an");
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.INFO;
    }
}