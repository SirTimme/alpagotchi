package bot.components.buttons;

import bot.components.buttons.delete.DeleteAccept;
import bot.components.buttons.delete.DeleteCancel;
import bot.components.buttons.init.InitAccept;
import bot.components.buttons.init.InitCancel;
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
        final var btnName = event.getComponentId().split(":")[1];
        final var btn = buttons.get(btnName);

        btn.execute(event);
    }
}