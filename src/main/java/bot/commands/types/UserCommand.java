package bot.commands.types;

import bot.commands.ISlashCommand;
import bot.db.IDatabase;
import bot.models.User;
import bot.utils.Responses;
import bot.utils.Utils;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Locale;

public abstract class UserCommand implements ISlashCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        final var user = IDatabase.INSTANCE.getUserById(event.getUser().getIdLong());
        final var locale = Utils.retrieveLocale(event);

        if (!event.getName().equals("init") && user == null) {
            event.reply(Responses.getLocalizedResponse("general.error.noAlpaca", locale)).setEphemeral(true).queue();
            return;
        }

        execute(event, locale, user);
    }

    protected abstract void execute(final SlashCommandInteractionEvent event, final Locale locale, final User user);
}