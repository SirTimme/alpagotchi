package dev.sirtimme.alpagotchi.components.buttons;

import dev.sirtimme.alpagotchi.components.buttons.delete.DeleteAccept;
import dev.sirtimme.alpagotchi.components.buttons.delete.DeleteCancel;
import dev.sirtimme.alpagotchi.components.buttons.init.InitAccept;
import dev.sirtimme.alpagotchi.components.buttons.init.InitCancel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Map;
import java.util.TreeMap;

public class ButtonManager {
    private final Map<String, IButton> buttons;

    public ButtonManager() {
        this.buttons = new TreeMap<>();
        this.buttons.put("initAccept", new InitAccept());
        this.buttons.put("initCancelled", new InitCancel());
        this.buttons.put("deleteAccept", new DeleteAccept());
        this.buttons.put("deleteCancelled", new DeleteCancel());
    }

    public void handle(final ButtonInteractionEvent event) {
        final var buttonName = event.getComponentId().split(":")[1];
        final var button = buttons.get(buttonName);

        button.execute(event);
    }
}