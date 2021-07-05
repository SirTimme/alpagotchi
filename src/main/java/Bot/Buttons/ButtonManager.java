package Bot.Buttons;

import com.mongodb.lang.Nullable;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.util.HashMap;
import java.util.Map;

public class ButtonManager {
    private final Map<String, IButton> buttons = new HashMap<>();

    public ButtonManager() {
        this.buttons.put("acceptInit", new AcceptInit());
        this.buttons.put("declineInit", new DeclineInit());
        this.buttons.put("acceptDelete", new AcceptDelete());
        this.buttons.put("cancelDelete", new CancelDelete());
    }

    public void handle(ButtonClickEvent event) {
        IButton btn = getButton(event);

        if (btn != null) {
            btn.execute(event, event.getUser().getIdLong());
        }
    }

    @Nullable
    private IButton getButton(ButtonClickEvent event) {
        return this.buttons.get(event.getComponentId());
    }
}
