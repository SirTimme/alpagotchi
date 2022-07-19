package bot.components.buttons.delete;

import bot.components.buttons.MessageButton;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Locale;

public class DeleteCancel extends MessageButton {
    @Override
    public void execute(final ButtonInteractionEvent event, final Locale locale) {
        final var format = new MessageFormat(Responses.get("dataCancelled", locale));
        final var msg = format.format(new Object[]{});

        event.editMessage(msg)
             .setActionRows(Collections.emptyList())
             .setEmbeds(Collections.emptyList())
             .queue();
    }
}