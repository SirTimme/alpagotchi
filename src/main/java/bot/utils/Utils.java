package bot.utils;

import bot.db.IDatabase;
import bot.models.guildsettings.GuildSettings;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import java.util.Locale;

public class Utils {
    public static Locale retrieveLocale(final SlashCommandInteractionEvent event) {
        return retrieveLocale(event.getGuild());
    }

    public static Locale retrieveLocale(final StringSelectInteractionEvent event) {
        return retrieveLocale(event.getGuild());
    }

    public static Locale retrieveLocale(final ButtonInteractionEvent event) {
        return retrieveLocale(event.getGuild());
    }

    private static Locale retrieveLocale(final Guild guild) {
        final var locale = Locale.ENGLISH;

        if (guild == null) {
            return locale;
        }

        final GuildSettings settings;
        if ((settings = IDatabase.INSTANCE.getSettingsById(guild.getIdLong())) == null) {
            return locale;
        } else {
            return settings.getLocale();
        }
    }
}