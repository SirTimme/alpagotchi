package bot.components.buttons;

import bot.db.IDatabase;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Locale;

public class BtnInitAccept implements IButton {
    @Override
    public void execute(final ButtonInteractionEvent event, final Locale locale) {
        IDatabase.INSTANCE.createUser(event.getUser().getIdLong());

        final var format = new MessageFormat(Responses.get("initSuccess", locale));
        final var msg = format.format(new Object[]{});

        event.editMessage(msg)
             .setActionRows(Collections.emptyList())
             .setEmbeds(Collections.emptyList())
             .queue();
    }
}