package bot.buttons.initialization;

import bot.buttons.IButton;
import bot.utils.Emote;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.util.Collections;

public class InitCancel implements IButton {
    @Override
    public void execute(ButtonClickEvent event, long authorID) {
        event.editMessage(Emote.REDCROSS + " Initialization cancelled")
             .setEmbeds(Collections.emptyList())
             .setActionRows(Collections.emptyList())
             .queue();
    }
}
