package bot.commands.member;

import bot.commands.interfaces.IDynamicUserCommand;
import bot.models.Entry;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.MessageFormat;
import java.util.Locale;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Outfit implements IDynamicUserCommand {
	@Override
	public Entry execute(final SlashCommandEvent event, final Entry user, final Locale locale) {
		final String outfit = event.getOption("outfit").getAsString();
		user.setOutfit(outfit);

		final MessageFormat msg = new MessageFormat(Responses.get("alpacaOutfit", locale));
		final String content = msg.format(new Object[]{ outfit });
		MessageService.reply(event, content, false);

		return user;
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
}
