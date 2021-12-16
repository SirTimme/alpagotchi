package bot.commands.member;

import bot.commands.interfaces.IStaticUserCommand;
import bot.models.Entry;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.Button;

import java.text.MessageFormat;
import java.util.Locale;

public class Delete implements IStaticUserCommand {
	@Override
	public CommandData getCommandData() {
		return new CommandData("delete", "Deletes your personal data");
	}

	@Override
	public void execute(final SlashCommandEvent event, final Entry user, final Locale locale) {
		final MessageFormat msg = new MessageFormat(Responses.get("dataDeletion", locale));
		final Button success = Button.success("acceptDelete", "Accept");
		final Button cancel = Button.danger("cancelDelete", "Cancel");

		MessageService.reply(event,	msg,true,	success, cancel);
	}
}
