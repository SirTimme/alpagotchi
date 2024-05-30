package dev.sirtimme.alpagotchi.components.menus;

import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

public interface IMenu {
    void execute(final StringSelectInteractionEvent event);
}