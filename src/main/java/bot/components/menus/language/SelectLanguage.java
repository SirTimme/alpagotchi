package bot.components.menus.language;

import bot.components.menus.MessageMenu;
import bot.db.IDatabase;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import java.util.Collections;
import java.util.Locale;
import java.util.Objects;

public class SelectLanguage extends MessageMenu {
    @Override
    public void handle(final StringSelectInteractionEvent event) {
        final var guildId = Objects.requireNonNull(event.getGuild()).getIdLong();
        final var settings = IDatabase.INSTANCE.getSettingsById(guildId);

        // Update db
        settings.setLanguage(event.getValues().get(0));
        IDatabase.INSTANCE.updateSettings(settings);

        event.editMessage(Responses.getLocalizedResponse("language", Locale.ENGLISH))
             .setComponents(Collections.emptyList())
             .setEmbeds(Collections.emptyList())
             .queue();
    }
}