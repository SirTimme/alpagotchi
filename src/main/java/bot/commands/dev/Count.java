package bot.commands.dev;

import bot.commands.interfaces.IDevCommand;
import bot.db.IDatabase;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.text.MessageFormat;

public class Count implements IDevCommand {
	@Override
	public void execute(SlashCommandEvent event) {
		final MessageFormat msg = new MessageFormat(Responses.get("count"));
		final String content = msg.format(new Object[]{ IDatabase.INSTANCE.getEntries(), event.getJDA().getGuilds().size() });

		MessageService.reply(event, content, false);
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData("count", "Counts all alpacas of Alpagotchi").setDefaultEnabled(false);
	}
}
