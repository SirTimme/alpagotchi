package bot.commands.member;

import bot.commands.ISlashCommand;
import bot.db.IDatabase;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.MessageFormat;
import java.util.Locale;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Outfit implements ISlashCommand {
	@Override
	public void execute(final SlashCommandInteractionEvent event, final Locale locale, final Entry user) {
		final var outfit = event.getOption("outfit").getAsString();

		user.setOutfit(outfit);
		IDatabase.INSTANCE.updateUser(user);

		final var format = new MessageFormat(Responses.get("alpacaOutfit", locale));
		final var msg = format.format(new Object[]{ outfit });

		event.reply(msg).queue();
	}

	@Override
	public CommandData getCommandData() {
		return Commands.slash("outfit", "Changes the appearance of your alpaca")
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
	public CommandType getCommandType() {
		return CommandType.USER;
	}
}