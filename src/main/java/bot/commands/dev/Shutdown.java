package bot.commands.dev;

import bot.commands.interfaces.IDevCommand;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.text.MessageFormat;
import java.util.Locale;

public class Shutdown implements IDevCommand {
	@Override
	public CommandData getCommandData() {
		return new CommandData("shutdown", "Shutdowns Alpagotchi").setDefaultEnabled(false);
	}

	@Override
	public void execute(SlashCommandEvent event, Locale locale) {
		final MessageFormat msg = new MessageFormat(Responses.get("shutdown", locale));
		final String content = msg.format(new Object[]{ event.getJDA().getSelfUser().getName() });

		MessageService.reply(event, content, false);
		event.getJDA().shutdown();
	}
}
