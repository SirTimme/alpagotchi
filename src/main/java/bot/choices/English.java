package bot.choices;

import bot.db.IDatabase;
import bot.models.GuildSettings;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;

import java.text.MessageFormat;
import java.util.Locale;

public class English implements IChoice {
	@Override
	public void execute(final SelectionMenuEvent event, final GuildSettings settings) {
		settings.setLocale(Locale.ENGLISH);
		IDatabase.INSTANCE.setGuildSettings(settings);

		MessageService.editReply(event, new MessageFormat(Responses.get("language", settings.getLocale())));
	}
}
