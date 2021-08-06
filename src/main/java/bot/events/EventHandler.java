package bot.events;

import bot.buttons.ButtonManager;
import bot.commands.SlashCommandManager;
import bot.db.IDatabase;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.UnavailableGuildJoinedEvent;
import net.dv8tion.jda.api.events.guild.UnavailableGuildLeaveEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventHandler extends ListenerAdapter {
    private final SlashCommandManager slashCommandManager;
    private final ButtonManager btnManager;

    public EventHandler() {
        this.slashCommandManager = new SlashCommandManager();
        this.btnManager = new ButtonManager();
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        IDatabase.INSTANCE.createGuild(event.getGuild().getIdLong());
    }

    @Override
    public void onUnavailableGuildJoined(UnavailableGuildJoinedEvent event) {
        IDatabase.INSTANCE.createGuild(event.getGuildIdLong());
    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        IDatabase.INSTANCE.deleteGuild(event.getGuild().getIdLong());
    }

    @Override
    public void onUnavailableGuildLeave(@NotNull UnavailableGuildLeaveEvent event) {
        IDatabase.INSTANCE.deleteGuild(event.getGuildIdLong());
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        slashCommandManager.handle(event);
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        btnManager.handle(event);
    }
}
