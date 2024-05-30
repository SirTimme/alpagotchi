package dev.sirtimme.alpagotchi.events;

import dev.sirtimme.alpagotchi.commands.CommandManager;
import dev.sirtimme.alpagotchi.components.buttons.ButtonManager;
import dev.sirtimme.alpagotchi.components.menus.MenuManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventHandler extends ListenerAdapter {
    private final CommandManager commandManager;
    private final MenuManager menuManager;
    private final ButtonManager buttonManager;

    public EventHandler() {
        this.commandManager = new CommandManager();
        this.menuManager = new MenuManager();
        this.buttonManager = new ButtonManager();
    }

    @Override
    public void onSlashCommandInteraction(final @NotNull SlashCommandInteractionEvent event) {
        this.commandManager.handle(event);
    }

    @Override
    public void onButtonInteraction(final @NotNull ButtonInteractionEvent event) {
        this.buttonManager.handle(event);
    }

    @Override
    public void onStringSelectInteraction(final @NotNull StringSelectInteractionEvent event) {
        this.menuManager.handle(event);
    }
}