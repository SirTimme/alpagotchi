package bot.components.menus;

import bot.db.IDatabase;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

import java.text.MessageFormat;
import java.util.Locale;

public abstract class MessageMenu implements IMenu {
    @Override
    public void execute(final SelectMenuInteractionEvent event) {
        final var locale = event.getGuild() == null
                ? Locale.ENGLISH
                : IDatabase.INSTANCE.getSettingsById(event.getGuild().getIdLong()).getLocale();

        final var authorId = event.getComponentId().split(":")[0];

        if (!authorId.equals(event.getUser().getId())) {
            final var format = new MessageFormat(Responses.get("errorNotCommandAuthor", locale));
            final var msg = format.format(new Object[]{});

            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        execute(event, locale);
    }

    protected abstract void execute(final SelectMenuInteractionEvent event, final Locale locale);
}