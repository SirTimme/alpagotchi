package bot.commands.member;

import bot.commands.UserCommand;
import bot.db.IDatabase;
import bot.models.Entry;
import bot.shop.Item;
import bot.shop.ItemManager;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.MessageFormat;
import java.util.Locale;

import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;
import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Buy extends UserCommand {
	private final ItemManager items;

	public Buy(ItemManager items) {
		this.items = items;
	}

	@Override
	public void execute(final SlashCommandEvent event, final Locale locale, final Entry user) {
		final int amount = (int) event.getOption("amount").getAsLong();
		if (amount > 10) {
			MessageService.queueReply(event, new MessageFormat(Responses.get("boughtTooManyItems", locale)), true);
			return;
		}

		final Item item = this.items.getItemByName(event.getOption("item").getAsString());

		final int price = amount * item.getPrice();
		final int balance = user.getCurrency();
		if (balance - price < 0) {
			final MessageFormat msg = new MessageFormat(Responses.get("insufficientBalance", locale));
			event.reply(msg.format(new Object[]{})).setEphemeral(true).queue();
			return;
		}

		user.setCurrency(balance - price);
		user.setItem(item.getName(), user.getItem(item.getName()) + amount);

		IDatabase.INSTANCE.updateUser(user);

		final MessageFormat msg = new MessageFormat(Responses.get("buySuccessful", locale));
		final String content = msg.format(new Object[]{ amount, item.getName(), price });

		MessageService.queueReply(event, content, false);
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
}
