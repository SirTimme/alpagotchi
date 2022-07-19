package bot.commands.member;

import bot.commands.CommandManager;
import bot.commands.InfoCommand;
import bot.utils.CommandType;
import bot.utils.Env;
import bot.utils.Responses;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.time.Instant;
import java.util.Locale;

public class Help extends InfoCommand {
    private final CommandManager commandManager;

    public Help(final CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale) {
        event.getJDA().retrieveUserById(Env.get("DEV_ID")).queue(dev -> {
            final var embed = new EmbedBuilder()
                    .setTitle(Responses.get("headerHelpEmbed", locale))
                    .setThumbnail("https://cdn.discordapp.com/attachments/795637300661977132/836542447186214942/avatar.png")
                    .addField("Commands", this.commandManager.getCommandNames(), true)
                    .addField("Need further help or found a bug?", "Join the [Alpagotchi Support](https://discord.gg/DXtYyzGhXR) server!", false)
                    .setFooter("Created by " + dev.getName(), dev.getAvatarUrl())
                    .setTimestamp(Instant.now())
                    .build();

            event.replyEmbeds(embed).queue();
        });
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("help", "Shows all commands of Alpagotchi")
                       .setDescriptionLocalization(DiscordLocale.GERMAN, "Zeigt alle Befehle von Alpagotchi");
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.INFO;
    }
}