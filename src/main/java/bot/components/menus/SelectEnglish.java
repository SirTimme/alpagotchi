package bot.components.menus;

import bot.components.IComponent;
import bot.db.IDatabase;
import bot.models.GuildSettings;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.GenericComponentInteractionCreateEvent;

import java.text.MessageFormat;
import java.util.Locale;

public class SelectEnglish implements IComponent {
	@Override
	public void execute(final GenericComponentInteractionCreateEvent event, final GuildSettings settings) {
		settings.setLocale(Locale.ENGLISH);
		IDatabase.INSTANCE.setGuildSettings(settings);

		MessageService.editReply(event, new MessageFormat(Responses.get("language", settings.getLocale())));
	}
}
