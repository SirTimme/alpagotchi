package bot.commands.dev;

import bot.commands.DevCommand;
import bot.models.Entry;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.text.MessageFormat;
import java.util.Locale;

public class Shutdown extends DevCommand {
	@Override
	public CommandData getCommandData() {
		return new CommandData("shutdown", "Shutdowns Alpagotchi").setDefaultEnabled(false);
	}

	@Override
	public void execute(final SlashCommandEvent event, final Locale locale, final Entry user) {
		final MessageFormat msg = new MessageFormat(Responses.get("shutdown", locale));
		final String content = msg.format(new Object[]{ event.getJDA().getSelfUser().getName() });

		MessageService.completeReply(event, content, false);

		event.getJDA().shutdown();
	}
}
