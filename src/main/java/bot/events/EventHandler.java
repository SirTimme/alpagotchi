package bot.events;

import bot.commands.CommandManager;
import bot.components.buttons.ButtonManager;
import bot.components.menus.MenuManager;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventHandler extends ListenerAdapter {
    private final CommandManager commands = new CommandManager();
    private final MenuManager menus = new MenuManager();
    private final ButtonManager buttons = new ButtonManager();

    @Override
    public void onSlashCommand(@NotNull final SlashCommandEvent event) {
        this.commands.handle(event);
    }

    @Override
    public void onButtonClick(@NotNull final ButtonClickEvent event) {
        this.buttons.handle(event);
    }

    @Override
    public void onSelectionMenu(@NotNull final SelectionMenuEvent event) {
        this.menus.handle(event);
    }
}
