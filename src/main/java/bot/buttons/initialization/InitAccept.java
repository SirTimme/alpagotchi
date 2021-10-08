package bot.buttons.initialization;

import bot.buttons.IButton;
import bot.db.IDatabase;
import bot.utils.Emote;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.util.Collections;

public class InitAccept implements IButton {
    @Override
    public void execute(ButtonClickEvent event, long authorID) {
        IDatabase.INSTANCE.createUser(authorID);

        event.editMessage(Emote.GREENTICK + " Your alpaca has been set up")
             .setEmbeds(Collections.emptyList())
             .setActionRows(Collections.emptyList())
             .queue();
    }
}
