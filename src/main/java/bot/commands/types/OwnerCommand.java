package bot.commands.types;

import bot.commands.ISlashCommand;
import bot.localization.LocaleUtils;
import bot.localization.LocalizedResponse;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Locale;

public abstract class OwnerCommand implements ISlashCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        final var locale = LocaleUtils.getLocale(event);

        if (!event.getUser().getId().equals(System.getenv("OWNER_ID"))) {
            event.reply(LocalizedResponse.get("general.error.noOwner", locale)).setEphemeral(true).queue();
            return;
        }

        execute(event, locale);
    }

    protected abstract void execute(final SlashCommandInteractionEvent event, final Locale locale);
}