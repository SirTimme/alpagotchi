package dev.sirtimme.alpagotchi.commands.types;

import dev.sirtimme.alpagotchi.commands.ISlashCommand;
import dev.sirtimme.alpagotchi.localization.LocaleUtils;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Locale;

public abstract class InfoCommand implements ISlashCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        final var locale = LocaleUtils.getLocale(event);

        execute(event, locale);
    }

    protected abstract void execute(final SlashCommandInteractionEvent event, final Locale locale);
}