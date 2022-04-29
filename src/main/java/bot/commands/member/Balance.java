package bot.commands.member;

import bot.commands.ISlashCommand;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.text.MessageFormat;
import java.util.Locale;

public class Balance implements ISlashCommand {
	@Override
    public void execute(final SlashCommandEvent event, final Locale locale, final Entry user) {
		final var msg = new MessageFormat(Responses.get("currentBalance", locale));
		final var content = msg.format(new Object[]{ user.getCurrency() });

		event.reply(content).queue();
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData("balance", "Shows your fluffy balance");
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.USER;
	}
}