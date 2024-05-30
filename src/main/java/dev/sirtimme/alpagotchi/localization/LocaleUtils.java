package dev.sirtimme.alpagotchi.localization;

import dev.sirtimme.alpagotchi.db.IDatabase;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import java.util.Locale;

public class LocaleUtils {
    public static Locale getLocale(final SlashCommandInteractionEvent event) {
        return getLocale(event.getGuild());
    }

    public static Locale getLocale(final StringSelectInteractionEvent event) {
        return getLocale(event.getGuild());
    }

    public static Locale getLocale(final ButtonInteractionEvent event) {
        return getLocale(event.getGuild());
    }

    private static Locale getLocale(final Guild guild) {
        final var locale = Locale.ENGLISH;
        if (guild == null) {
            return locale;
        }

        final var settings = IDatabase.INSTANCE.getSettingsById(guild.getIdLong());
        if (settings == null) {
            return locale;
        }

        return settings.getLocale();
    }
}