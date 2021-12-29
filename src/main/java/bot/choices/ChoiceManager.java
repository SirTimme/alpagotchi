package bot.choices;

import bot.db.IDatabase;
import bot.models.GuildSettings;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ChoiceManager {
	private final Map<String, IChoice> choices;

	public ChoiceManager() {
		this.choices = new HashMap<>(){{
			put("lang_english", new English());
			put("lang_german", new German());
		}};
	}

	public void handle(final SelectionMenuEvent event) {
		final Guild guild = event.getGuild();
		if (guild == null) {
			MessageService.editReply(event, new MessageFormat(Responses.get("guildOnly", Locale.ENGLISH)));
			return;
		}

		final GuildSettings settings = IDatabase.INSTANCE.getGuildSettings(guild.getIdLong());

		this.choices.get(event.getValues().get(0)).execute(event, settings);
	}
}
