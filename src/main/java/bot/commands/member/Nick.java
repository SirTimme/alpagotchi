package bot.commands.member;

import bot.commands.UserCommand;
import bot.db.IDatabase;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.MessageFormat;
import java.util.Locale;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Nick extends UserCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final Entry user) {
        final var nickname = event.getOption("nickname").getAsString();
        if (nickname.length() > 256) {
            final var format = new MessageFormat(Responses.get("nicknameTooLong", locale));
            final var msg = format.format(new Object[]{});

            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        user.setNickname(nickname);
        IDatabase.INSTANCE.updateUser(user);

        final var format = new MessageFormat(Responses.get("nicknameSuccessful", locale));
        final var msg = format.format(new Object[]{ nickname });

        event.reply(msg).queue();
    }

    @Override
    public CommandData getCommandData() {
        final var option = new OptionData(STRING, "nickname", "The new nickname", true)
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