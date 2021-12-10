package bot.choices;

import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;

import java.util.HashMap;
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
		this.choices.get(event.getValues().get(0)).execute(event);
	}
}
