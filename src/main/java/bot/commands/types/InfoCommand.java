package bot.commands.types;

import bot.commands.ISlashCommand;
import bot.localization.LocaleUtils;
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