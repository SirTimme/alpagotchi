package bot.commands.dev;

import bot.commands.IInfoCommand;
import bot.db.IDatabase;
import bot.utils.Resources;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.text.MessageFormat;

public class Count implements IInfoCommand {
	@Override
	public void execute(SlashCommandEvent event) {
		final MessageFormat msg = new MessageFormat(Resources.getPattern("count"));
		event.reply(msg.format(new Object[]{ IDatabase.INSTANCE.getEntries(), event.getJDA().getGuilds().size() })).queue();
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData("count", "Counts all alpacas of Alpagotchi").setDefaultEnabled(false);
	}
}
