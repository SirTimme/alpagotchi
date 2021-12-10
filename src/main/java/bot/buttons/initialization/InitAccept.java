package bot.buttons.initialization;

import bot.buttons.IButton;
import bot.db.IDatabase;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Locale;

public class InitAccept implements IButton {
    @Override
    public void execute(final ButtonClickEvent event, final Locale locale) {
        IDatabase.INSTANCE.createUser(event.getUser().getIdLong());
        MessageService.edit(event, new MessageFormat(Responses.get("initSuccess", locale)));
    }
}
