package bot.components.menus.language;

import bot.components.menus.MessageMenu;
import bot.db.IDatabase;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

import java.util.Collections;
import java.util.Locale;

public class SelectLanguage extends MessageMenu {
    @Override
    public void execute(final SelectMenuInteractionEvent event, final Locale locale) {
        final var settings = IDatabase.INSTANCE.getSettingsById(event.getGuild().getIdLong());
        settings.setLanguage(event.getValues().get(0));

        IDatabase.INSTANCE.updateSettings(settings);

        final var msg = Responses.get("language", settings.getLocale());

        event.editMessage(msg).setActionRows(Collections.emptyList()).setEmbeds(Collections.emptyList()).queue();
    }
}