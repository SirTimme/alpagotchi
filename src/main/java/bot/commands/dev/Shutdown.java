package bot.commands.dev;

import bot.commands.ISlashCommand;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.text.MessageFormat;
import java.util.Locale;

public class Shutdown implements ISlashCommand {
	@Override
	public void execute(final SlashCommandEvent event, final Locale locale, final Entry user) {
		final var format = new MessageFormat(Responses.get("shutdown", locale));
		final var msg = format.format(new Object[]{ event.getJDA().getSelfUser().getName() });

		event.reply(msg).complete();
		event.getJDA().shutdown();
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData("shutdown", "Shutdowns Alpagotchi").setDefaultEnabled(false);
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.DEV;
	}
}