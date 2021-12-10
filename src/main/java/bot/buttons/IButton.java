package bot.buttons;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.util.Locale;

public interface IButton {
    void execute(final ButtonClickEvent event, final Locale locale);
}
