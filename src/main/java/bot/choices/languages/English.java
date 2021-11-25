package bot.choices.languages;

import bot.choices.ISelectionChoice;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;

import java.util.Collections;
import java.util.Locale;

public class English implements ISelectionChoice {
	@Override
	public void execute(SelectionMenuEvent event) {
		Locale.setDefault(new Locale("en"));
		event.editMessage("\uD83C\uDDFA\uD83C\uDDF8 The server language has been changed to **english**").setActionRows(Collections.emptyList()).queue();
	}
}
