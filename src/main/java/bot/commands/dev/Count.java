package bot.commands.dev;

import bot.commands.DevCommand;
import bot.db.IDatabase;
import bot.models.Entry;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.text.MessageFormat;
import java.util.Locale;

public class Count extends DevCommand {
	@Override
	public void execute(SlashCommandEvent event, final Locale locale, final Entry user) {
		final MessageFormat msg = new MessageFormat(Responses.get("count", locale));
		final String content = msg.format(new Object[]{ IDatabase.INSTANCE.getEntries(), event.getJDA().getGuilds().size() });

		MessageService.queueReply(event, content, false);
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData("count", "Counts all alpacas of Alpagotchi").setDefaultEnabled(false);
	}
}
