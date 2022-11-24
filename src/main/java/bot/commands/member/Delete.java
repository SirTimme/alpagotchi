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

import java.util.Locale;

public class Delete extends UserCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final Entry user) {
        final var userId = event.getUser().getIdLong();

        final var btnSuccess = Button.success(userId + ":deleteAccept", Responses.getLocalizedResponse("buttonAccept", locale));
        final var btnCancel = Button.danger(userId + ":deleteCancelled", Responses.getLocalizedResponse("buttonCancel", locale));

        final var msg = Responses.getLocalizedResponse("deleteWarning", locale);

        event.reply(msg).addActionRow(btnSuccess, btnCancel).queue();
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