package bot.components.menus;

import bot.db.IDatabase;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MenuManager {
    private static final Map<String, IMenu> menus = new HashMap<>();

    public void handle(final SelectMenuInteractionEvent event) {
        final IMenu menu = menus.get(event.getComponentId());
        final Locale locale = getLocale(event);

        menu.execute(event, locale);
        menus.remove(event.getComponentId());
    }

    public static void addMenu(final String key, final IMenu menu) {
        menus.put(key, menu);
    }

    private Locale getLocale(final SelectMenuInteractionEvent event) {
        return event.getGuild() == null
                ? Locale.ENGLISH
                : IDatabase.INSTANCE.getGuildSettings(event.getGuild().getIdLong()).getLocale();
    }
}