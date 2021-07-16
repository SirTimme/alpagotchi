package Bot.Buttons;

import Bot.Database.IDatabase;
import Bot.Utils.Emote;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.util.Collections;

public class AcceptInit implements IButton {
    @Override
    public void execute(ButtonClickEvent event, long authorID) {
        IDatabase.INSTANCE.createUser(authorID);

        event.editMessage(Emote.GREENTICK + " Your alpaca has been set up")
             .setEmbeds(Collections.emptyList())
             .setActionRows(Collections.emptyList())
             .queue();
    }
}
