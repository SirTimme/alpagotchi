package Bot.Buttons;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

public interface IButton {
    void execute(ButtonClickEvent event, long authorID);
}
