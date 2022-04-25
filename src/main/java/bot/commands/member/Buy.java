package bot.commands.member;

import bot.commands.ISlashCommand;
import bot.db.IDatabase;
import bot.models.Entry;
import bot.shop.ItemManager;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.MessageFormat;
import java.util.Locale;

import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;
import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Buy implements ISlashCommand {
	private final ItemManager items;

	public Buy(final ItemManager items) {
		this.items = items;
	}

	@Override
	public void execute(final SlashCommandEvent event, final Locale locale, final Entry user) {
		final var amount = (int) event.getOption("amount").getAsLong();

		if (amount > 10) {
			final var format = new MessageFormat(Responses.get("boughtTooManyItems", locale));
			final var msg = format.format(new Object[] {});

			event.reply(msg).setEphemeral(true).queue();
			return;
		}

		final var item = this.items.getItemByName(event.getOption("item").getAsString());

		final var price = amount * item.getPrice();
		final var balance = user.getCurrency();

		if (balance - price < 0) {
			final var format = new MessageFormat(Responses.get("insufficientBalance", locale));
			final var msg = format.format(new Object[] {});

			event.reply(msg).setEphemeral(true).queue();
			return;
		}

		user.setCurrency(balance - price);
		user.setItem(item.getName(), user.getItem(item.getName()) + amount);

		IDatabase.INSTANCE.updateUser(user);

		final var format = new MessageFormat(Responses.get("buySuccessful", locale));
		final var msg = format.format(new Object[]{ amount, item.getName(), price });

		event.reply(msg).queue();
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData("buy", "Buys your alpaca items from the shop")
				.addOptions(
						new OptionData(STRING, "item", "The item to buy", true)
								.addChoices(
										new Command.Choice("salad", "salad"),
										new Command.Choice("taco", "taco"),
										new Command.Choice("steak", "steak"),
										new Command.Choice("water", "water"),
										new Command.Choice("lemonade", "lemonade"),
										new Command.Choice("cacao", "cacao")
								),
						new OptionData(INTEGER, "amount", "The amount of items", true)
				);
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.USER;
	}
}