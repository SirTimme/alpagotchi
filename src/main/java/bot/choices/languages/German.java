package bot.choices.languages;

import bot.choices.ISelectionChoice;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;

import java.util.Collections;

public class German implements ISelectionChoice {
	@Override
	public void execute(SelectionMenuEvent event) {
		event.editMessage("Es wurde " + event.getValues().get(0) + " angeklickt").setActionRows(Collections.emptyList()).queue();
	}
}
