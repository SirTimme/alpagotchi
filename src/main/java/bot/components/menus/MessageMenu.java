package bot.components.menus;

import bot.db.IDatabase;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

import java.util.Locale;

public abstract class MessageMenu implements IMenu {
    @Override
    public void execute(final SelectMenuInteractionEvent event) {
        final var locale = event.getGuild() == null
                ? Locale.ENGLISH
                : IDatabase.INSTANCE.getSettingsById(event.getGuild().getIdLong()).getLocale();

        execute(event, locale);
    }

    protected abstract void execute(final SelectMenuInteractionEvent event, final Locale locale);
}