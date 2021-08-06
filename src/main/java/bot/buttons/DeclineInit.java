package bot.buttons;

import bot.utils.Emote;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.util.Collections;

public class DeclineInit implements IButton {
    @Override
    public void execute(ButtonClickEvent event, long authorID) {
        event.editMessage(Emote.REDCROSS + " Initialization declined")
             .setEmbeds(Collections.emptyList())
             .setActionRows(Collections.emptyList())
             .queue();
    }
}
