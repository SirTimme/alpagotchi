package bot.components.menus;

import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

import java.util.Locale;

public interface IMenu {
    void execute(final SelectMenuInteractionEvent event, final Locale locale);
}