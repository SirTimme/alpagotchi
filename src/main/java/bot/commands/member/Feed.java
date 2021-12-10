package bot.commands.member;

import bot.commands.interfaces.IDynamicUserCommand;
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

public class Feed implements IDynamicUserCommand {
	private final ItemManager itemMan;

	public Feed(ItemManager itemMan) {
		this.itemMan = itemMan;
	}

	@Override
	public Entry execute(final SlashCommandEvent event, final Entry user, final Locale locale) {
		final long remainingSleep = user.getSleepAsMinutes();
		if (remainingSleep > 0) {
			final MessageFormat msg = new MessageFormat(Responses.get("sleep", locale));
			MessageService.reply(event, msg.format(new Object[]{ remainingSleep }), true);
			return null;
		}

		final int itemAmount = (int) event.getOption("amount").getAsLong();
		if (itemAmount > 5) {
			MessageService.reply(event, new MessageFormat(Responses.get("fedTooManyItems", locale)), true);
			return null;
		}

		final Item item = this.itemMan.getItemByName(event.getOption("item").getAsString());
		final int userItems = user.getItem(item.getName()) - itemAmount;
		if (userItems < 0) {
			event.reply(Emote.REDCROSS + " You don't own that many items").setEphemeral(true).queue();

			return null;
		}
		final int oldValue = item.getStat().equals("hunger") ? user.getHunger() : user.getThirst();
		final int itemSaturation = itemAmount * item.getSaturation();
		if (oldValue + itemSaturation > 100) {
			event.reply(Emote.REDCROSS + " You would overfeed your alpaca").setEphemeral(true).queue();

			return null;
		}

		user.setItem(item.getName(), userItems);

		final String pattern;
		if (item.getStat().equals("hunger")) {
			user.setHunger(oldValue + itemSaturation);
			pattern = Responses.get("eat", locale);
		}
		else {
			user.setThirst(oldValue + itemSaturation);
			pattern = Responses.get("drink", locale);
		}
		event.reply(new MessageFormat(pattern).format(new Object[]{ itemAmount, item.getName(), itemSaturation })).queue();
		return user;
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
}
