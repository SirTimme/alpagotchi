package bot.commands.member;

import bot.commands.ISlashCommand;
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

public class Delete implements ISlashCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final Entry user) {
        final var btnSuccess = Button.success(UUID.randomUUID().toString(), "Accept");
        final var btnCancel = Button.danger(UUID.randomUUID().toString(), "Cancel");

        ButtonManager.addButton(btnSuccess.getId(), new BtnDeleteAccept());
        ButtonManager.addButton(btnCancel.getId(), new BtnDeleteCancel());

        final var format = new MessageFormat(Responses.get("dataDeletion", locale));
        final var msg = format.format(new Object());

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