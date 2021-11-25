package bot.choices.languages;

import bot.choices.ISelectionChoice;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;

import java.util.Collections;
import java.util.Locale;

public class German implements ISelectionChoice {
	@Override
	public void execute(SelectionMenuEvent event) {
		Locale.setDefault(new Locale("de"));
		event.editMessage("\uD83C\uDDE9\uD83C\uDDEA Die Serversprache wurde auf **deutsch** gesetzt").setActionRows(Collections.emptyList()).queue();
	}
}
