package bot.commands.member;

import bot.commands.interfaces.IStaticUserCommand;
import bot.models.Entry;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.text.MessageFormat;
import java.util.Locale;

public class Balance implements IStaticUserCommand {
	@Override
	public void execute(SlashCommandEvent event, Entry user) {
		final MessageFormat msg = new MessageFormat(Responses.get("balance", new Locale("en-us")));
		final String content = msg.format(new Object[]{ user.getCurrency() });

		MessageService.reply(event, content, false);
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData("balance", "Shows your fluffy balance");
	}
}
