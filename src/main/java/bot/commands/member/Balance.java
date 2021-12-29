package bot.commands.member;

import bot.commands.SlashCommand;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.text.MessageFormat;
import java.util.Locale;

public class Balance extends SlashCommand {
	@Override
	public CommandData getCommandData() {
		return new CommandData("balance", "Shows your fluffy balance");
	}

	@Override
	protected CommandType getCommandType() {
		return CommandType.USER;
	}

	@Override
	public void execute(final SlashCommandEvent event, final Locale locale, final Entry user) {
		final MessageFormat msg = new MessageFormat(Responses.get("currentBalance", locale));
		final String content = msg.format(new Object[]{ user.getCurrency() });

		MessageService.queueReply(event, content, false);
	}
}
