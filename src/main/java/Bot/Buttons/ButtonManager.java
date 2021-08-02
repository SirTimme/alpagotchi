package Bot.Buttons;

import com.mongodb.lang.Nullable;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.util.HashMap;
import java.util.Map;

public class ButtonManager {
    private final Map<String, IButton> buttons = new HashMap<>();

    public ButtonManager() {
        buttons.put("acceptInit", new AcceptInit());
        buttons.put("declineInit", new DeclineInit());
        buttons.put("acceptDelete", new AcceptDelete());
        buttons.put("cancelDelete", new CancelDelete());
    }

    public void handle(ButtonClickEvent event) {
        final IButton btn = getButton(event);

        btn.execute(event, event.getUser().getIdLong());
    }

    private IButton getButton(ButtonClickEvent event) {
        return buttons.get(event.getComponentId());
    }
}
