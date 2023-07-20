package bot.commands;

import bot.db.IDatabase;
import bot.models.User;
import bot.utils.Responses;
import bot.utils.Utils;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Locale;

public abstract class UserSlashCommand implements ISlashCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        final var user = IDatabase.INSTANCE.getUserById(event.getUser().getIdLong());
        final var locale = Utils.retrieveLocale(event);

        if (!event.getName().equals("init") && user == null) {
            final var msg = Responses.getLocalizedResponse("errorAlpacaNotOwned", locale);
            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        execute(event, locale, user);
    }

    protected abstract void execute(final SlashCommandInteractionEvent event, final Locale locale, final User user);
}