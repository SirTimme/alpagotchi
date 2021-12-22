package bot.commands.member;

import bot.commands.SlashCommand;
import bot.db.IDatabase;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.MessageFormat;
import java.util.Locale;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Outfit extends SlashCommand {
	@Override
	public void execute(final SlashCommandEvent event, final Locale locale, final Entry user) {
		final String outfit = event.getOption("outfit").getAsString();

		user.setOutfit(outfit);
		IDatabase.INSTANCE.updateUser(user);

		final MessageFormat msg = new MessageFormat(Responses.get("alpacaOutfit", locale));
		final String content = msg.format(new Object[]{ outfit });

		MessageService.queueReply(event, content, false);
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData("outfit", "Changes the appearance of your alpaca")
				.addOptions(
						new OptionData(STRING, "outfit", "The new outfit of your alpaca", true)
								.addChoices(
										new Command.Choice("default", "default"),
										new Command.Choice("gentleman", "gentleman"),
										new Command.Choice("lady", "lady")
								)
				);
	}

	@Override
	protected CommandType getCommandType() {
		return CommandType.USER;
	}
}
