package bot.choices;

import bot.choices.languages.English;
import bot.choices.languages.German;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;

import java.util.HashMap;
import java.util.Map;

public class SelectionChoiceManager {
	private final Map<String, ISelectionChoice> selectionChoices = new HashMap<>();

	public SelectionChoiceManager() {
		selectionChoices.put("lang_english", new English());
		selectionChoices.put("lang_german", new German());
	}

	public void handle(SelectionMenuEvent event) {
		selectionChoices.get(event.getValues().get(0)).execute(event);
	}
}
