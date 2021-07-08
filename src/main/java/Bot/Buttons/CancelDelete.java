package Bot.Buttons;

import Bot.Utils.Emote;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.util.Collections;

public class CancelDelete implements IButton {
    @Override
    public void execute(ButtonClickEvent event, long authorID) {
        event.editMessage(Emote.REDCROSS + " Data deletion cancelled")
             .setActionRows(Collections.emptyList())
             .queue();
    }
}
