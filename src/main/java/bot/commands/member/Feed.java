package bot.commands.member;

import bot.commands.ISlashCommand;
import bot.db.IDatabase;
import bot.models.Entry;
import bot.shop.ItemManager;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;

import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;
import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Feed implements ISlashCommand {
	private final ItemManager items;

	public Feed(final ItemManager items) {
		this.items = items;
	}

	@Override
	public void execute(final SlashCommandInteractionEvent event, final Locale locale, final Entry user) {
		final var remainingSleep = user.getSleep();

		if (remainingSleep > 0) {
			final var format = new MessageFormat(Responses.get("alpacaSleeping", locale));
			final var msg = format.format(new Object[]{ remainingSleep });

			event.reply(msg).setEphemeral(true).queue();
			return;
		}

		final var itemAmount = event.getOption("amount").getAsInt();
		final var item = this.items.getItemByName(event.getOption("item").getAsString());
		final var newItemAmount = user.getItem(item.getName()) - itemAmount;

		if (newItemAmount < 0) {
			final var format = new MessageFormat(Responses.get("notEnoughItems", locale));
			final var msg = format.format(new Object[]{ });

			event.reply(msg).setEphemeral(true).queue();
			return;
		}

		final var oldValue = user.getStat(item.getStat());
		final var saturation = itemAmount * item.getSaturation();

		if (oldValue + saturation > 100) {
			final var format = new MessageFormat(Responses.get("alpacaOverfeeded", locale));
			final var msg = format.format(new Object[]{ });

			event.reply(msg).setEphemeral(true).queue();
			return;
		}

		user.setItem(item.getName(), newItemAmount);
		user.setStat(item.getStat(), oldValue + saturation);

		IDatabase.INSTANCE.updateUser(user);

		final var format = new MessageFormat(Responses.get(item.getStat(), locale));
		final var msg = format.format(new Object[]{ itemAmount, item.getName(), saturation });

		event.reply(msg).queue();
	}

	@Override
	public CommandData getCommandData() {
		final var choices = List.of(
				new Command.Choice("salad", "salad"),
				new Command.Choice("taco", "taco"),
				new Command.Choice("steak", "steak"),
				new Command.Choice("water", "water"),
				new Command.Choice("lemonade", "lemonade"),
				new Command.Choice("cacao", "cacao")
		);

		final var options = List.of(
				new OptionData(STRING, "item", "The item to feed", true).addChoices(choices),
				new OptionData(INTEGER, "amount", "The amount of items", true).setRequiredRange(1, 5)
		);

		return Commands.slash("feed", "Feeds your alpaca items")
					   .addOptions(options);
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.USER;
	}
}