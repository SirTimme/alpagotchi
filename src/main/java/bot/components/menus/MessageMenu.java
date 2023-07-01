package bot.components.menus;

import bot.db.IDatabase;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import java.util.Locale;

public abstract class MessageMenu implements IMenu {
    @Override
    public void execute(final StringSelectInteractionEvent event) {
        final var locale = Locale.ENGLISH;

        final var authorId = event.getComponentId().split(":")[0];

        if (!authorId.equals(event.getUser().getId())) {
            final var msg = Responses.getLocalizedResponse("errorNotCommandAuthor", locale);

            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        handle(event);
    }

    protected abstract void handle(final StringSelectInteractionEvent event);
}