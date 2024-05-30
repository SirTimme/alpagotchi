package dev.sirtimme.alpagotchi.components.menus.language;

import dev.sirtimme.alpagotchi.components.menus.MessageMenu;
import dev.sirtimme.alpagotchi.db.IDatabase;
import dev.sirtimme.alpagotchi.models.guildsettings.GuildSettings;
import dev.sirtimme.alpagotchi.localization.LocalizedResponse;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import java.util.Collections;
import java.util.Locale;

public class SelectLanguage extends MessageMenu {
    @Override
    public void handle(final StringSelectInteractionEvent event) {
        final var settings = IDatabase.INSTANCE.getSettingsById(event.getGuild().getIdLong());
        final var locale = Locale.forLanguageTag(event.getValues().get(0));

        // guild is not saved in the database
        if (settings == null) {
            IDatabase.INSTANCE.updateSettings(new GuildSettings(event.getGuild().getIdLong(), locale));

            event.editMessage(LocalizedResponse.get("language.successful", locale))
                 .setComponents(Collections.emptyList())
                 .setEmbeds(Collections.emptyList())
                 .queue();
            return;
        }

        // update db
        settings.setLocale(locale);
        IDatabase.INSTANCE.updateSettings(settings);

        // reply to the user
        event.editMessage(LocalizedResponse.get("language.successful", locale))
             .setComponents(Collections.emptyList())
             .setEmbeds(Collections.emptyList())
             .queue();
    }
}