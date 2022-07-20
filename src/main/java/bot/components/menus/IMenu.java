package bot.components.menus;

import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

public interface IMenu {
    void execute(final SelectMenuInteractionEvent event);
}