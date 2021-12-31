package bot.components.menus;

import bot.utils.MessageService;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MenuManager {
    private static final Map<String, IMenu> menus = new HashMap<>();

    public void handle(final SelectionMenuEvent event) {
        final IMenu menu = menus.get(event.getComponentId());
        final Locale locale = MessageService.getLocale(event);

        menu.execute(event, locale);
        menus.remove(event.getComponentId());
    }

    public static void addMenu(final String key, final IMenu menu) {
        menus.put(key, menu);
    }
}
