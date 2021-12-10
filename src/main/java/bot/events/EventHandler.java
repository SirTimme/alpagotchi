package bot.events;

import bot.buttons.ButtonManager;
import bot.commands.CommandManager;
import bot.choices.ChoiceManager;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * Handles discord events
 */
public class EventHandler extends ListenerAdapter {
    private final CommandManager commands; // Stores bot commands
    private final ButtonManager buttons; // Stores button commands
    private final ChoiceManager choices; // Stores menu commands

    /**
     * Default constructor
     */
    public EventHandler() {
        this.commands = new CommandManager();
        this.buttons = new ButtonManager();
        this.choices = new ChoiceManager();
    }

    /**
     * Slashcommand was executed
     * @param event contains data about the executed command
     */
    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        this.commands.handle(event);
    }

    /**
     * Button was clicked
     * @param event contains data about the clicked button
     */
    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        this.buttons.handle(event);
    }

    /**
     * Selection in menu was made
     * @param event contains data about the selected choice
     */
    @Override
    public void onSelectionMenu(@NotNull SelectionMenuEvent event) {
        this.choices.handle(event);
    }
}
