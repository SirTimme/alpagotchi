package bot.components.menus;

import bot.db.IDatabase;
import bot.models.GuildSettings;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;

import java.text.MessageFormat;
import java.util.Locale;

public class MenuLanguage implements IMenu {
    @Override
    public void execute(final SelectionMenuEvent event, final Locale locale) {
        final GuildSettings settings = IDatabase.INSTANCE.getGuildSettings(event.getGuild().getIdLong());
        settings.setLanguage(event.getValues().get(0));

        IDatabase.INSTANCE.updateGuildSettings(settings);

        MessageService.editReply(event, new MessageFormat(Responses.get("language", settings.getLocale())));
    }
}