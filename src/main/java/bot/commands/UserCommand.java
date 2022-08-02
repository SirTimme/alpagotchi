package bot.commands;

import bot.db.IDatabase;
import bot.models.Entry;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.text.MessageFormat;
import java.util.Locale;

public abstract class UserCommand implements ISlashCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        final var user = IDatabase.INSTANCE.getUserById(event.getUser().getIdLong());
        final var locale = event.getGuild() == null
                ? Locale.ENGLISH
                : IDatabase.INSTANCE.getSettingsById(event.getGuild().getIdLong()).getLocale();

        if (!event.getName().equals("init") && user == null) {
            final var format = new MessageFormat(Responses.get("errorAlpacaNotOwned", locale));
            final var msg = format.format(new Object[]{});

            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        execute(event, locale, user);
    }

    protected abstract void execute(final SlashCommandInteractionEvent event, final Locale locale, final Entry user);
}