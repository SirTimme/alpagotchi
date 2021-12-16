package bot.buttons.deletion;

import bot.buttons.IButton;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.text.MessageFormat;
import java.util.Locale;

public class DeleteCancel implements IButton {
    @Override
    public void execute(ButtonClickEvent event, final Locale locale) {
        MessageService.editReply(event, new MessageFormat(Responses.get("dataCancelled", locale)));
    }
}
