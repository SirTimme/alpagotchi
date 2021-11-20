package bot.events;

import bot.buttons.ButtonManager;
import bot.commands.SlashCommandManager;
import bot.choices.SelectionChoiceManager;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventHandler extends ListenerAdapter {
    private final SlashCommandManager slashcommands;
    private final ButtonManager buttons;
    private final SelectionChoiceManager selectionChoices;

    public EventHandler() {
        this.slashcommands = new SlashCommandManager();
        this.buttons = new ButtonManager();
        this.selectionChoices = new SelectionChoiceManager();
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        slashcommands.handle(event);
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        buttons.handle(event);
    }

    @Override
    public void onSelectionMenu(@NotNull SelectionMenuEvent event) {
        selectionChoices.handle(event);
    }
}
