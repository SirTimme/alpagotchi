package bot.commands.member;

import bot.commands.ISlashCommand;
import bot.db.IDatabase;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.MessageFormat;
import java.util.Locale;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Nick implements ISlashCommand {
	@Override
	public void execute(final SlashCommandEvent event, final Locale locale, final Entry user) {
		final String nickname = event.getOption("nickname").getAsString();
		if (nickname.length() > 256) {
			MessageService.queueReply(event, new MessageFormat(Responses.get("nicknameTooLong", locale)), true);
			return;
		}

		user.setNickname(nickname);
		IDatabase.INSTANCE.updateUser(user);

		final MessageFormat msg = new MessageFormat(Responses.get("nicknameSuccessful", locale));
		final String content = msg.format(new Object[]{ nickname });

		MessageService.queueReply(event, content, false);
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData("nick", "Gives your alpaca a new nickname")
				.addOptions(
						new OptionData(STRING, "nickname", "The new nickname of the alpaca", true)
				);
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.USER;
	}
}
