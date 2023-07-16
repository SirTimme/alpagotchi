package bot.components.buttons;

import bot.utils.Responses;
import bot.utils.Utils;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Locale;

public abstract class MessageButton implements IButton {
    @Override
    public void execute(final ButtonInteractionEvent event) {
        final var locale = Utils.retrieveLocale(event);

        final var authorId = event.getComponentId().split(":")[0];

        if (!authorId.equals(event.getUser().getId())) {
            final var msg = Responses.getLocalizedResponse("errorNotCommandAuthor", locale);
            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        execute(event, locale);
    }

    protected abstract void execute(final ButtonInteractionEvent event, final Locale locale);
}