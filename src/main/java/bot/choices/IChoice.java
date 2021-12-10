package bot.choices;

import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;

public interface IChoice {
	void execute(final SelectionMenuEvent event);
}
