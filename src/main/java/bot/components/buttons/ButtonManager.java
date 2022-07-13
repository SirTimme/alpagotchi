package bot.components.buttons;

import bot.db.IDatabase;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ButtonManager {
    private static final Map<String, IButton> buttons = new HashMap<>();

    public static void addButton(final String key, final IButton button) {
        buttons.put(key, button);
    }

    public void handle(final ButtonInteractionEvent event) {
        final IButton button = buttons.get(event.getComponentId());
        final Locale locale = getLocale(event);

        button.execute(event, locale);

        final Set<String> keys = event.getMessage()
                                      .getButtons()
                                      .stream()
                                      .map(Button::getId)
                                      .collect(Collectors.toSet());

        buttons.keySet().removeAll(keys);
    }

    private Locale getLocale(final ButtonInteractionEvent event) {
        return event.getGuild() == null
                ? Locale.ENGLISH
                : IDatabase.INSTANCE.getGuildSettings(event.getGuild().getIdLong()).getLocale();
    }
}