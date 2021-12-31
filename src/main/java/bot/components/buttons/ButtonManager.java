package bot.components.buttons;

import bot.utils.MessageService;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.*;
import java.util.stream.Collectors;

public class ButtonManager {
    private static final Map<String, IButton> buttons = new HashMap<>();

    public void handle(final ButtonClickEvent event) {
        final IButton button = buttons.get(event.getComponentId());
        final Locale locale = MessageService.getLocale(event);

        button.execute(event, locale);

        final Set<String> keys = event.getMessage()
                                      .getButtons()
                                      .stream()
                                      .map(Button::getId)
                                      .collect(Collectors.toSet());

        buttons.keySet().removeAll(keys);
    }

    public static void addButton(final String key, final IButton button) {
        buttons.put(key, button);
    }
}
