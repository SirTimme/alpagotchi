package bot.components.buttons;

import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.text.MessageFormat;
import java.util.Locale;

public class BtnDeleteCancel implements IButton {
    @Override
    public void execute(final ButtonClickEvent event, final Locale locale) {
        MessageService.editReply(event, new MessageFormat(Responses.get("dataCancelled", locale)));
    }
}
