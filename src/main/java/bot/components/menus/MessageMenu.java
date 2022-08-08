package bot.components.menus;

import bot.db.IDatabase;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

import java.util.Locale;

public abstract class MessageMenu implements IMenu {
    @Override
    public void execute(final SelectMenuInteractionEvent event) {
        final var locale = event.getGuild() == null
                ? Locale.ENGLISH
                : IDatabase.INSTANCE.getSettingsById(event.getGuild().getIdLong()).getLocale();

        final var authorId = event.getComponentId().split(":")[0];

        if (!authorId.equals(event.getUser().getId())) {
            final var msg = Responses.get("errorNotCommandAuthor", locale);

            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        handle(event);
    }

    protected abstract void handle(final SelectMenuInteractionEvent event);
}