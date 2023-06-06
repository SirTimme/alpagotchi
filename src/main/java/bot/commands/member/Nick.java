package bot.commands.member;

import bot.commands.MutableUserCommand;
import bot.db.IDatabase;
import bot.models.User;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;

public class Nick extends MutableUserCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final User user) {
        // selected nickname
        final var nickname = event.getOption("nickname").getAsString();

        // discord only allows for 256 char long embed titles
        if (nickname.length() > 256) {
            final var msg = Responses.getLocalizedResponse("nicknameTooLong", locale);
            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        // update nickname
        user.getAlpaca().setNickname(nickname);

        // reply to the user
        final var format = new MessageFormat(Responses.getLocalizedResponse("nickname", locale));
        final var msg = format.format(new Object[]{ nickname });
        event.reply(msg).queue();
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