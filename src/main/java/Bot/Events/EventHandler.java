package Bot.Events;

import Bot.Buttons.ButtonManager;
import Bot.Command.SlashCommandManager;
import Bot.Database.IDatabase;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.UnavailableGuildJoinedEvent;
import net.dv8tion.jda.api.events.guild.UnavailableGuildLeaveEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

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
        String[] split = event.getMessage().getContentRaw()
                              .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                              .split("\\s+");

        final boolean validCmd = slashCommandManager.getCommands()
                                                    .keySet()
                                                    .stream()
                                                    .anyMatch(cmd -> cmd.equals(split[0].toLowerCase()));

        if (event.getMessage().getContentRaw().startsWith(prefix) && validCmd) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Big slashcommand update!")
              .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
              .setDescription("Hey you!\n" +
                      "I had a huge slash command update and now im working with those.\n" +
                      "Simply enter **/** in your bar and you will see all my normal commands there.\n" +
                      "Have fun and thanks for choosing me.")
              .addField("Need further help?", "Join the [Alpagotchi Support](https://discord.gg/DXtYyzGhXR) server!", false);

            try {
                event.getChannel().sendMessage(eb.build()).queue();
            } catch (PermissionException error) {
                event.getChannel()
                     .sendMessage("__**Big slashcommand update!**__\nHey you!\n" +
                             "I had a huge slash command update and now im working with those.\n" +
                             "Simply enter **/** in your bar and you will see all my normal commands there.\n" +
                             "Have fun and thanks for choosing me.\n" +
                             "Need further help? Join the Alpagotchi Support Server at <https://discord.gg/DXtYyzGhXR>")
                     .queue();
            }
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
        slashCommandManager.handle(event);
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        btnManager.handle(event);
    }
}
