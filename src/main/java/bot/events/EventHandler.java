package bot.events;

import bot.commands.CommandManager;
import bot.components.ComponentManager;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * Handles discord events
 */
public class EventHandler extends ListenerAdapter {
    private final CommandManager commands = new CommandManager();
    private final ComponentManager components = new ComponentManager();

    /**
     * Slashcommand was executed
     * @param event contains data about the executed command
     */
    @Override
    public void onSlashCommand(@NotNull final SlashCommandEvent event) {
        this.commands.handle(event);
    }

    @Override
    public void onGenericComponentInteractionCreate(@NotNull GenericComponentInteractionCreateEvent event) {
        this.components.handle(event);
    }
}
