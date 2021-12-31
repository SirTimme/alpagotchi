package bot.components.menus;

import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;

import java.util.Locale;

public interface IMenu {
    void execute(final SelectionMenuEvent event, final Locale locale);
}
