package dev.sirtimme.alpagotchi.commands.member;

import dev.sirtimme.alpagotchi.commands.types.UserCommand;
import dev.sirtimme.alpagotchi.db.IDatabase;
import dev.sirtimme.alpagotchi.models.user.User;
import dev.sirtimme.alpagotchi.commands.types.CommandType;
import dev.sirtimme.alpagotchi.localization.LocalizedResponse;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Locale;

public class Nick extends UserCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final User user) {
        // selected nickname
        final var nickname = event.getOption("nickname").getAsString();

        // discord only allows for 256 char long embed titles
        if (nickname.length() > 256) {
            event.reply(LocalizedResponse.get("nickname.error.tooLong", locale)).setEphemeral(true).queue();
            return;
        }

        // update nickname
        user.getAlpaca().setNickname(nickname);

        // update db
        IDatabase.INSTANCE.updateUser(user);

        // reply to the user
        event.reply(LocalizedResponse.get("nickname.successful", locale, nickname)).queue();
    }

    @Override
    public CommandData getCommandData() {
        final var option = new OptionData(OptionType.STRING, "nickname", "The new nickname", true)
                .setDescriptionLocalization(DiscordLocale.GERMAN, "Der neue Spitzname");

        return Commands.slash("nick", "Gives your alpaca a new nickname")
                       .setDescriptionLocalization(DiscordLocale.GERMAN, "Gibt deinem Alpaka einen neuen Spitznamen")
                       .addOptions(option);
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.USER;
    }
}