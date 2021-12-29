package bot.choices;

import bot.models.GuildSettings;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;

public interface IChoice {
	void execute(final SelectionMenuEvent event, final GuildSettings settings);
}
