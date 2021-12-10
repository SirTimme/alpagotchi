package bot.buttons.initialization;

import bot.buttons.IButton;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.text.MessageFormat;
import java.util.Locale;

public class InitCancel implements IButton {
    @Override
    public void execute(final ButtonClickEvent event, final Locale locale) {
        MessageService.edit(event, new MessageFormat(Responses.get("initCancelled", locale)));
    }
}
