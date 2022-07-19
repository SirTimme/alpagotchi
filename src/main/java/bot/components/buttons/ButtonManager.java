package bot.components.buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.*;

public class ButtonManager {
    private final Map<String, IButton> buttons;

    public ButtonManager() {
        this.buttons = new TreeMap<>();
        this.buttons.put("initAccept", new BtnInitAccept());
        this.buttons.put("initCancelled", new BtnInitCancel());
        this.buttons.put("deleteAccept", new BtnDeleteAccept());
        this.buttons.put("deleteCancelled", new BtnDeleteCancel());
    }

    public void handle(final ButtonInteractionEvent event) {
        final var authorId = event.getComponentId().split(":")[0];
        final var btnName = event.getComponentId().split(":")[1];

        if (!authorId.equals(event.getUser().getId())) {
            return;
        }

        final var btn = buttons.get(btnName);

        btn.execute(event);
    }
}