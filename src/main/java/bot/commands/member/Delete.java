package bot.commands.member;

import bot.commands.UserCommand;
import bot.components.buttons.BtnDeleteAccept;
import bot.components.buttons.BtnDeleteCancel;
import bot.components.buttons.ButtonManager;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.UUID;

public class Delete extends UserCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final Entry user) {
        final var userId = event.getUser().getIdLong();

        final var btnSuccess = Button.success(userId + ":deleteAccept", "Accept");
        final var btnCancel = Button.danger(userId + ":deleteCancelled", "Cancel");

        final var format = new MessageFormat(Responses.get("dataDeletion", locale));
        final var msg = format.format(new Object[]{});

        event.reply(msg)
             .addActionRow(btnSuccess, btnCancel)
             .setEphemeral(true)
             .queue();
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("delete", "Deletes your personal data");
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.INFO;
    }
}