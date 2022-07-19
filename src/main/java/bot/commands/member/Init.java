package bot.commands.member;

import bot.commands.UserCommand;
import bot.components.buttons.BtnInitAccept;
import bot.components.buttons.BtnInitCancel;
import bot.components.buttons.ButtonManager;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.Env;
import bot.utils.Responses;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

public class Init extends UserCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final Entry user) {
        if (user != null) {
            final var format = new MessageFormat(Responses.get("alpacaAlreadyOwned", locale));
            final var msg = format.format(new Object[]{});

            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        final var userId = event.getUser().getIdLong();

        final var btnAccept = Button.success(userId + ":initAccept", "Accept");
        final var btnCancel = Button.danger(userId + ":initCancelled", "Decline");

        event.getJDA().retrieveUserById(Env.get("DEV_ID")).queue(dev -> {
            final var embed = new EmbedBuilder()
                    .setTitle(Responses.get("userInformation", locale))
                    .setDescription(Responses.get("initIntro", locale))
                    .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                    .addField(Responses.get("headerStorageId", locale), Responses.get("bodyStorageId", locale), false)
                    .addField(Responses.get("headerDeletionId", locale), Responses.get("bodyDeletionId", locale), false)
                    .setFooter("Created by " + dev.getName(), dev.getAvatarUrl())
                    .setTimestamp(Instant.now())
                    .build();

            event.replyEmbeds(embed).addActionRow(btnAccept, btnCancel).queue();
        });
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("init", "Initializes a new alpaca");
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.INIT;
    }
}