package bot.commands.dev;

import bot.commands.IInfoCommand;
import bot.utils.Resources;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.text.MessageFormat;

public class Shutdown implements IInfoCommand {
	@Override
	public void execute(SlashCommandEvent event) {
		final MessageFormat msg = new MessageFormat(Resources.getPattern("shutdown"));
		event.reply(msg.format(new Object[]{ event.getJDA().getSelfUser().getName() })).queue();

		event.getJDA().shutdown();
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData("shutdown", "Shutdowns Alpagotchi").setDefaultEnabled(false);
	}
}
