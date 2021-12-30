package bot.components;

import bot.models.GuildSettings;
import net.dv8tion.jda.api.events.interaction.GenericComponentInteractionCreateEvent;

public interface IComponent {
    void execute(final GenericComponentInteractionCreateEvent event, final GuildSettings settings);
}
