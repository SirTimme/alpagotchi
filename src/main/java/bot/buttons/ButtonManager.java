package bot.buttons;

import bot.buttons.delete.DeleteAccept;
import bot.buttons.delete.DeleteCancel;
import bot.buttons.initialize.InitAccept;
import bot.buttons.initialize.InitCancel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.util.HashMap;
import java.util.Map;

public class ButtonManager {
    private final Map<String, IButton> buttons = new HashMap<>();

    public ButtonManager() {
        buttons.put("acceptInit", new InitAccept());
        buttons.put("declineInit", new InitCancel());
        buttons.put("acceptDelete", new DeleteAccept());
        buttons.put("cancelDelete", new DeleteCancel());
    }

    public void handle(ButtonClickEvent event) {
        buttons.get(event.getComponentId()).execute(event, event.getUser().getIdLong());
    }
}
