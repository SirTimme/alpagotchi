package dev.sirtimme.alpagotchi.commands.member;

import dev.sirtimme.alpagotchi.commands.types.UserCommand;
import dev.sirtimme.alpagotchi.models.user.User;
import dev.sirtimme.alpagotchi.commands.types.CommandType;
import dev.sirtimme.alpagotchi.localization.LocalizedResponse;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Locale;

public class Delete extends UserCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final User user) {
        final var userId = event.getUser().getIdLong();

        final var btnSuccess = Button.success(userId + ":deleteAccept", LocalizedResponse.get("button.accept", locale));
        final var btnCancel = Button.danger(userId + ":deleteCancelled", LocalizedResponse.get("button.cancel", locale));

        event.reply(LocalizedResponse.get("delete.warning", locale)).addComponents(ActionRow.of(btnSuccess, btnCancel)).queue();
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