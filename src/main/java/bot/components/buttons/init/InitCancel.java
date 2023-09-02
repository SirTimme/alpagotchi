package bot.components.buttons.init;

import bot.components.buttons.MessageButton;
import bot.localization.LocalizedResponse;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Collections;
import java.util.Locale;

public class InitCancel extends MessageButton {
    @Override
    public void execute(final ButtonInteractionEvent event, final Locale locale) {
        event.editMessage(LocalizedResponse.get("init.cancelled", locale))
             .setComponents(Collections.emptyList())
             .setEmbeds(Collections.emptyList())
             .queue();
    }
}