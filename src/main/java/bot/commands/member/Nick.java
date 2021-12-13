package bot.commands.member;

import bot.commands.interfaces.IDynamicUserCommand;
import bot.models.Entry;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.MessageFormat;
import java.util.Locale;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Nick implements IDynamicUserCommand {
	@Override
	public Entry execute(final SlashCommandEvent event, final Entry user, final Locale locale) {
		final String nickname = event.getOption("nickname").getAsString();
		if (nickname.length() > 256) {
			MessageService.reply(event, new MessageFormat(Responses.get("nicknameTooLong", locale)), true);
			return null;
		}

		user.setNickname(nickname);

		final MessageFormat msg = new MessageFormat(Responses.get("nicknameSuccessful", locale));
		final String content = msg.format(new Object[]{ nickname });
		MessageService.reply(event, content, false);

		return user;
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData("nick", "Gives your alpaca a new nickname")
				.addOptions(
						new OptionData(STRING, "nickname", "The new nickname of the alpaca", true)
				);
	}
}
