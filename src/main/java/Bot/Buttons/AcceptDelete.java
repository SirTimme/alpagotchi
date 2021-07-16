package Bot.Buttons;

import Bot.Database.IDatabase;
import Bot.Utils.Emote;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.util.Collections;

public class AcceptDelete implements IButton {
    @Override
    public void execute(ButtonClickEvent event, long authorID) {
        IDatabase.INSTANCE.deleteUser(authorID);

        event.editMessage(Emote.GREENTICK + " Data successfully deleted")
             .setActionRows(Collections.emptyList())
             .queue();
    }
}
