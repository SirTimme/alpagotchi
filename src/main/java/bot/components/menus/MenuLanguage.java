package bot.components.menus;

import bot.db.IDatabase;
import bot.models.GuildSettings;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Locale;

public class MenuLanguage implements IMenu {
    @Override
    public void execute(final SelectMenuInteractionEvent event, final Locale locale) {
        final GuildSettings settings = IDatabase.INSTANCE.getGuildSettings(event.getGuild().getIdLong());
        settings.setLanguage(event.getValues().get(0));

        IDatabase.INSTANCE.updateGuildSettings(settings);

        final var format = new MessageFormat(Responses.get("language", settings.getLocale()));
        final var msg = format.format(new Object());

        event.editMessage(msg)
             .setActionRows(Collections.emptyList())
             .setEmbeds(Collections.emptyList())
             .queue();
    }
}