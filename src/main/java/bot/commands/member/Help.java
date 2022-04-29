package bot.commands.member;

import bot.commands.CommandManager;
import bot.commands.ISlashCommand;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.Env;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.time.Instant;
import java.util.Locale;

public class Help implements ISlashCommand {
    private final CommandManager commands;

    public Help(final CommandManager commands) {
        this.commands = commands;
    }

    @Override
    public void execute(final SlashCommandEvent event, final Locale locale, final Entry user) {
        event.getJDA().retrieveUserById(Env.get("DEV_ID")).queue(dev -> {
            final var embed = new EmbedBuilder()
                    .setTitle(Responses.get("headerHelpEmbed", locale))
                    .setThumbnail("https://cdn.discordapp.com/attachments/795637300661977132/836542447186214942/avatar.png")
                    .addField("Commands", this.commands.getCommandsAsString(), true)
                    .addField("Need further help or found a bug?", "Join the [Alpagotchi Support](https://discord.gg/DXtYyzGhXR) server!", false)
                    .setFooter("Created by " + dev.getName(), dev.getAvatarUrl())
                    .setTimestamp(Instant.now())
                    .build();

            event.replyEmbeds(embed).queue();
        });
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("help", "Shows all commands of Alpagotchi");
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.INFO;
    }
}