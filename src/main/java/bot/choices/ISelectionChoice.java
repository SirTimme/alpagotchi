package bot.choices;

import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;

public interface ISelectionChoice {
	void execute(SelectionMenuEvent event);
}
