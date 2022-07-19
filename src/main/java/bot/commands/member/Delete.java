package bot.commands.member;

import bot.commands.UserCommand;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.text.MessageFormat;
import java.util.Locale;

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
        return Commands.slash("delete", "Deletes your saved data")
                       .setDescriptionLocalization(DiscordLocale.GERMAN, "Löscht deine gespeicherten Daten");
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.INFO;
    }
}