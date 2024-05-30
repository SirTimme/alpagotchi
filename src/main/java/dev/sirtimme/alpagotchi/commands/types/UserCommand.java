package dev.sirtimme.alpagotchi.commands.types;

import dev.sirtimme.alpagotchi.commands.ISlashCommand;
import dev.sirtimme.alpagotchi.db.IDatabase;
import dev.sirtimme.alpagotchi.localization.LocaleUtils;
import dev.sirtimme.alpagotchi.models.user.User;
import dev.sirtimme.alpagotchi.localization.LocalizedResponse;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Locale;

public abstract class UserCommand implements ISlashCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        final var user = IDatabase.INSTANCE.getUserById(event.getUser().getIdLong());
        final var locale = LocaleUtils.getLocale(event);

        if (!event.getName().equals("init") && user == null) {
            event.reply(LocalizedResponse.get("general.error.noAlpaca", locale)).setEphemeral(true).queue();
            return;
        }

        execute(event, locale, user);
    }

    protected abstract void execute(final SlashCommandInteractionEvent event, final Locale locale, final User user);
}