package bot.components.buttons.init;

import bot.components.buttons.MessageButton;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Collections;
import java.util.Locale;

public class InitCancel extends MessageButton {
    @Override
    public void execute(final ButtonInteractionEvent event, final Locale locale) {
        event.editMessage(Responses.get("initCancelled", locale))
             .setComponents(Collections.emptyList())
             .setEmbeds(Collections.emptyList())
             .queue();
    }
}