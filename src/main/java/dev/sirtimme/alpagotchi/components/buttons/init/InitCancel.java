package dev.sirtimme.alpagotchi.components.buttons.init;

import dev.sirtimme.alpagotchi.components.buttons.MessageButton;
import dev.sirtimme.alpagotchi.localization.LocalizedResponse;
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