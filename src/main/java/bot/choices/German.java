package bot.choices;

import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;

import java.text.MessageFormat;
import java.util.Locale;

public class German implements IChoice {
	@Override
	public void execute(final SelectionMenuEvent event) {
		MessageService.editReply(event, new MessageFormat(Responses.get("language", new Locale("de"))));
	}
}
