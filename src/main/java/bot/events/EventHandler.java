package bot.events;

import bot.commands.CommandManager;
import bot.components.buttons.ButtonManager;
import bot.components.menus.MenuManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventHandler extends ListenerAdapter {
    private final CommandManager commands = new CommandManager();
    private final MenuManager menus = new MenuManager();
    private final ButtonManager buttons = new ButtonManager();

    @Override
    public void onSlashCommandInteraction(final @NotNull SlashCommandInteractionEvent event) {
        this.commands.handle(event);
    }

    @Override
    public void onButtonInteraction(final @NotNull ButtonInteractionEvent event) {
        this.buttons.handle(event);
    }

    @Override
    public void onSelectMenuInteraction(final @NotNull SelectMenuInteractionEvent event) {
        this.menus.handle(event);
    }
}