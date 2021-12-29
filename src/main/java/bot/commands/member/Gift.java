package bot.commands.member;

import bot.commands.SlashCommand;
import bot.db.IDatabase;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.MessageFormat;
import java.util.Locale;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;


public class Gift extends SlashCommand {
	@Override
	public void execute(final SlashCommandEvent event, final Locale locale, final Entry user) {
		final User selectedUser = event.getOption("user").getAsUser();
		if (selectedUser.getIdLong() == user.getMemberID()) {
			MessageService.queueReply(event, new MessageFormat(Responses.get("giftedYourself", locale)), true);
			return;
		}

		final Entry selectedDBUser = IDatabase.INSTANCE.getUser(selectedUser.getIdLong());
		if (selectedDBUser == null) {
			MessageService.queueReply(event, new MessageFormat(Responses.get("giftedUserNotInitialized", locale)), true);
			return;
		}

		final int amount = (int) event.getOption("amount").getAsLong();
		if (amount > 5) {
			MessageService.queueReply(event, new MessageFormat(Responses.get("giftedTooManyItems", locale)), true);
			return;
		}

		final String selectedItem = event.getOption("item").getAsString();
		if (user.getItem(selectedItem) - amount < 0) {
			MessageService.queueReply(event, new MessageFormat(Responses.get("notEnoughItems", locale)), true);
			return;
		}

		user.setItem(selectedItem, user.getItem(selectedItem) - amount);
		selectedDBUser.setItem(selectedItem, selectedDBUser.getItem(selectedItem) + amount);

		IDatabase.INSTANCE.updateUser(selectedDBUser);
		IDatabase.INSTANCE.updateUser(user);

		final MessageFormat msg = new MessageFormat(Responses.get("giftSuccessful", locale));
		final String content = msg.format(new Object[]{ amount, selectedItem, selectedUser.getName() });

		MessageService.queueReply(event, content, false);
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData("gift", "Gifts another user items")
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
						new OptionData(INTEGER, "amount", "The amount of gifted items", true)
				);
	}

	@Override
	protected CommandType getCommandType() {
		return CommandType.USER;
	}
}
