package bot.components.buttons.init;

import bot.components.buttons.MessageButton;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Collections;
import java.util.Locale;

public class InitCancel extends MessageButton {
    @Override
    public void execute(final ButtonInteractionEvent event, final Locale locale) {
        final var msg = Responses.get("initCancelled", locale);

        event.editMessage(msg).setActionRows(Collections.emptyList()).setEmbeds(Collections.emptyList()).queue();
    }
}