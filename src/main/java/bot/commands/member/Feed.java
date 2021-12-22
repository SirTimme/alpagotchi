package bot.commands.member;

import bot.commands.SlashCommand;
import bot.db.IDatabase;
import bot.models.Entry;
import bot.shop.Item;
import bot.shop.ItemManager;
import bot.utils.CommandType;
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

public class Feed extends SlashCommand {
	private final ItemManager items;

	public Feed(final ItemManager items) {
		this.items = items;
	}

	@Override
	public void execute(final SlashCommandEvent event, final Locale locale, final Entry user) {
		final long remainingSleep = user.getSleep();
		if (remainingSleep > 0) {
			final MessageFormat msg = new MessageFormat(Responses.get("alpacaSleeping", locale));
			final String content = msg.format(new Object[]{ remainingSleep });

			MessageService.queueReply(event, content, true);
			return;
		}

		final int itemAmount = (int) event.getOption("amount").getAsLong();
		if (itemAmount > 5) {
			final MessageFormat msg = new MessageFormat(Responses.get("fedTooManyItems", locale));

			MessageService.queueReply(event, msg, true);
			return;
		}

		final Item item = this.items.getItemByName(event.getOption("item").getAsString());
		final int newItemAmount = user.getItem(item.getName()) - itemAmount;
		if (newItemAmount < 0) {
			final MessageFormat msg = new MessageFormat(Responses.get("notEnoughItems", locale));

			MessageService.queueReply(event, msg, true);
			return;
		}

		final int oldValue = user.getStat(item.getStat());
		final int saturation = itemAmount * item.getSaturation();
		if (oldValue + saturation > 100) {
			final MessageFormat msg = new MessageFormat(Responses.get("alpacaOverfeeded", locale));

			MessageService.queueReply(event, msg, true);
			return;
		}

		user.setItem(item.getName(), newItemAmount);
		user.setStat(item.getStat(), oldValue + saturation);

		IDatabase.INSTANCE.updateUser(user);

		final MessageFormat msg = new MessageFormat(Responses.get(item.getStat(), locale));
		final String content = msg.format(new Object[]{ itemAmount, item.getName(), saturation });

		MessageService.queueReply(event, content, true);
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData("feed", "Feeds your alpaca items")
				.addOptions(
						new OptionData(STRING, "item", "The item to feed", true)
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
	protected CommandType getCommandType() {
		return CommandType.USER;
	}
}
