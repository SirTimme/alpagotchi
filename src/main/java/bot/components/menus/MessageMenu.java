package bot.components.menus;

import bot.localization.LocaleUtils;
import bot.localization.LocalizedResponse;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

public abstract class MessageMenu implements IMenu {
    @Override
    public void execute(final StringSelectInteractionEvent event) {
        final var locale = LocaleUtils.getLocale(event);

        final var authorId = event.getComponentId().split(":")[0];
        if (!authorId.equals(event.getUser().getId())) {
            final var msg = LocalizedResponse.get("general.error.notCommandAuthor", locale);
            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        handle(event);
    }

    protected abstract void handle(final StringSelectInteractionEvent event);
}