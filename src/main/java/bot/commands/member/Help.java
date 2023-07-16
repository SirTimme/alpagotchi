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
        event.getJDA().retrieveUserById(System.getenv("DEV_ID")).queue(dev -> {
            final var embed = new EmbedBuilder()
                    .setTitle(Responses.getLocalizedResponse("helpEmbedTitle", locale))
                    .setDescription("```\n" + this.commandManager.getCommandNames() + "```")
                    .addField(Responses.getLocalizedResponse("helpJoinFieldTitle", locale),
                              Responses.getLocalizedResponse("helpJoinFieldBody", locale),
                              false)
                    .setFooter(Responses.getLocalizedResponse("createdByNotice", locale), dev.getAvatarUrl())
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