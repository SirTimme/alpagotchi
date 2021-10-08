package bot.buttons.deletion;

import bot.buttons.IButton;
import bot.utils.Emote;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.util.Collections;

public class DeleteCancel implements IButton {
    @Override
    public void execute(ButtonClickEvent event, long authorID) {
        event.editMessage(Emote.REDCROSS + " Data deletion cancelled")
             .setActionRows(Collections.emptyList())
             .queue();
    }
}
