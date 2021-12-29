package bot.buttons;

import bot.utils.MessageService;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.util.HashMap;
import java.util.Map;

public class ButtonManager {
    private final Map<String, IButton> buttons;

    public ButtonManager() {
        this.buttons = new HashMap<>() {{
            put("acceptInit", new InitAccept());
            put("declineInit", new InitCancel());
            put("acceptDelete", new DeleteAccept());
            put("cancelDelete", new DeleteCancel());
        }};
    }

    public void handle(final ButtonClickEvent event) {
        this.buttons.get(event.getComponentId()).execute(event, MessageService.getLocale(event));
    }
}
