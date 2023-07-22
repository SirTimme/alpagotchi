package bot.components.menus.language;

import bot.components.menus.MessageMenu;
import bot.db.IDatabase;
import bot.models.GuildSettings;
import bot.utils.Responses;
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

            event.editMessage(Responses.getLocalizedResponse("language.successful", locale))
                 .setComponents(Collections.emptyList())
                 .setEmbeds(Collections.emptyList())
                 .queue();
            return;
        }

        // update db
        settings.setLocale(locale);
        IDatabase.INSTANCE.updateSettings(settings);

        // reply to the user
        event.editMessage(Responses.getLocalizedResponse("language.successful", locale))
             .setComponents(Collections.emptyList())
             .setEmbeds(Collections.emptyList())
             .queue();
    }
}