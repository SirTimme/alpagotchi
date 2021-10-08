package bot.buttons;

import bot.buttons.deletion.DeleteAccept;
import bot.buttons.deletion.DeleteCancel;
import bot.buttons.initialization.InitAccept;
import bot.buttons.initialization.InitCancel;
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
        final IButton btn = getButton(event);

        btn.execute(event, event.getUser().getIdLong());
    }

    private IButton getButton(ButtonClickEvent event) {
        return buttons.get(event.getComponentId());
    }
}
