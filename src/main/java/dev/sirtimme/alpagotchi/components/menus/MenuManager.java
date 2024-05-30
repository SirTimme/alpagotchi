package dev.sirtimme.alpagotchi.components.menus;

import dev.sirtimme.alpagotchi.components.menus.language.SelectLanguage;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import java.util.Map;
import java.util.TreeMap;

public class MenuManager {
    private final Map<String, IMenu> menus;

    public MenuManager() {
        this.menus = new TreeMap<>();
        this.menus.put("language", new SelectLanguage());
    }

    public void handle(final StringSelectInteractionEvent event) {
        final var menuName = event.getComponentId().split(":")[1];
        final var menu = menus.get(menuName);

        menu.execute(event);
    }
}