package bot.buttons.deletion;

import bot.buttons.IButton;
import bot.db.IDatabase;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.text.MessageFormat;
import java.util.Locale;

public class DeleteAccept implements IButton {
    @Override
    public void execute(final ButtonClickEvent event, final Locale locale) {
        IDatabase.INSTANCE.deleteUser(event.getUser().getIdLong());
        MessageService.editReply(event, new MessageFormat(Responses.get("dataSuccess", locale)));
    }
}
