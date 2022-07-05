package bot.components.buttons;

import bot.db.IDatabase;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.text.MessageFormat;
import java.util.Locale;

public class BtnInitAccept implements IButton {
    @Override
    public void execute(final ButtonClickEvent event, final Locale locale) {
        IDatabase.INSTANCE.createUser(event.getUser().getIdLong());

        MessageService.editReply(event, new MessageFormat(Responses.get("initSuccess", locale)));
    }
}