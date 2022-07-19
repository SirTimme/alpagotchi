package bot.components.menus;

import bot.components.menus.language.SelectLanguage;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

import java.util.Map;
import java.util.TreeMap;

public class MenuManager {
    private final Map<String, IMenu> menus;

    public MenuManager() {
        this.menus = new TreeMap<>();
        this.menus.put("language", new SelectLanguage());
    }

    public void handle(final SelectMenuInteractionEvent event) {
        final var authorId = event.getComponentId().split(":")[0];
        final var menuName = event.getComponentId().split(":")[1];

        if (!authorId.equals(event.getUser().getId())) {
            return;
        }

        final var menu = menus.get(menuName);

        menu.execute(event);
    }
}