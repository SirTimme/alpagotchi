package Bot.Events;

import Bot.Buttons.ButtonManager;
import Bot.Command.SlashCommandManager;
import Bot.Database.IDatabase;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.UnavailableGuildJoinedEvent;
import net.dv8tion.jda.api.events.guild.UnavailableGuildLeaveEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
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
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        final String prefix = IDatabase.INSTANCE.getPrefix(event.getGuild().getIdLong());

        if (event.getMessage().getContentRaw().startsWith(prefix)) {
            event.getChannel().sendMessage("Hey you!\n" +
                    "I had a huge slash command update and now im working with those. " +
                    "Simply enter **/** in your bar and you see all my normal commands there.\n" +
                    "Have fun and thanks for choosing me.\n" +
                    "If you need additional help, you can join my support discord with this link <https://discord.gg/DXtYyzGhXR>").queue();
        }
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
        System.out.println(event.getCommandIdLong());
        slashCommandManager.handle(event);
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        btnManager.handle(event);
    }
}
