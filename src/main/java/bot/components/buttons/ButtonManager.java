package bot.components.buttons;

import bot.components.buttons.delete.DeleteAccept;
import bot.components.buttons.delete.DeleteCancel;
import bot.components.buttons.init.InitAccept;
import bot.components.buttons.init.InitCancel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.*;

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
        final var authorId = event.getComponentId().split(":")[0];
        final var btnName = event.getComponentId().split(":")[1];

        if (!authorId.equals(event.getUser().getId())) {
            return;
        }

        final var btn = buttons.get(btnName);

        btn.execute(event);
    }
}