package bot.components.buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Locale;

public interface IButton {
    void execute(final ButtonInteractionEvent event, final Locale locale);
}