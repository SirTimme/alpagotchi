package bot.commands.member;

import bot.commands.ISlashCommand;
import bot.db.IDatabase;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.MessageFormat;
import java.util.Locale;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Nick implements ISlashCommand {
	@Override
	public void execute(final SlashCommandInteractionEvent event, final Locale locale, final Entry user) {
		final String nickname = event.getOption("nickname").getAsString();
		if (nickname.length() > 256) {
			final var format = new MessageFormat(Responses.get("nicknameTooLong", locale));
			final var msg = format.format(new Object());

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
		return Commands.slash("nick", "Gives your alpaca a new nickname")
					   .addOptions(new OptionData(STRING, "nickname", "The new nickname of the alpaca", true));
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.USER;
	}
}