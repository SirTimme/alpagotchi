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

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class Gift implements ISlashCommand {
	@Override
	public void execute(final SlashCommandInteractionEvent event, final Locale locale, final Entry user) {
		final var selectedUser = event.getOption("user").getAsUser();

		if (selectedUser.getIdLong() == user.getMemberID()) {
			final var format = new MessageFormat(Responses.get("giftedYourself", locale));
			final var msg = format.format(new Object[] {});

			event.reply(msg).setEphemeral(true).queue();
			return;
		}

		final var selectedDBUser = IDatabase.INSTANCE.getUser(selectedUser.getIdLong());

		if (selectedDBUser == null) {
			final var format = new MessageFormat(Responses.get("giftedUserNotInitialized", locale));
			final var msg = format.format(new Object[] {});

			event.reply(msg).setEphemeral(true).queue();
			return;
		}

		final var amount = (int) event.getOption("amount").getAsLong();
		final var selectedItem = event.getOption("item").getAsString();

		if (user.getItem(selectedItem) - amount < 0) {
			final var format = new MessageFormat(Responses.get("notEnoughItems", locale));
			final var msg = format.format(new Object[] {});

			event.reply(msg).setEphemeral(true).queue();
			return;
		}

		user.setItem(selectedItem, user.getItem(selectedItem) - amount);
		selectedDBUser.setItem(selectedItem, selectedDBUser.getItem(selectedItem) + amount);

		IDatabase.INSTANCE.updateUser(selectedDBUser);
		IDatabase.INSTANCE.updateUser(user);

		final var format = new MessageFormat(Responses.get("giftSuccessful", locale));
		final var msg = format.format(new Object[]{ amount, selectedItem, selectedUser.getName() });

		event.reply(msg).queue();
	}

	@Override
	public CommandData getCommandData() {
		return Commands.slash("gift", "Gifts another user items")
				.addOptions(
						new OptionData(USER, "user", "The user you want to gift to", true),
						new OptionData(STRING, "item", "The item to gift", true)
								.addChoices(
										new Command.Choice("salad", "salad"),
										new Command.Choice("taco", "taco"),
										new Command.Choice("steak", "steak"),
										new Command.Choice("water", "water"),
										new Command.Choice("lemonade", "lemonade"),
										new Command.Choice("cacao", "cacao")
								),
						new OptionData(INTEGER, "amount", "The amount of gifted items", true).setRequiredRange(1,5)
				);
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.USER;
	}
}