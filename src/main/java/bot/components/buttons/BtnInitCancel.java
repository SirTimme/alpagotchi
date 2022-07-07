package bot.components.buttons;

import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Locale;

public class BtnInitCancel implements IButton {
    @Override
    public void execute(final ButtonInteractionEvent event, final Locale locale) {
        final var format = new MessageFormat(Responses.get("initCancelled", locale));
        final var msg = format.format(new Object[]{});

        event.editMessage(msg)
             .setActionRows(Collections.emptyList())
             .setEmbeds(Collections.emptyList())
             .queue();
    }
}