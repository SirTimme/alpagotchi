package bot.commands.member;

import bot.commands.UserCommand;
import bot.db.IDatabase;
import bot.models.Entry;
import bot.shop.Item;
import bot.shop.ItemManager;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;
import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Feed extends UserCommand {
    private final ItemManager itemManager;

    public Feed(final ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final Entry user) {
        final var remainingSleep = user.getSleep();

        if (remainingSleep > 0) {
            final var format = new MessageFormat(Responses.get("sleepCurrentlySleeping", locale));
            final var msg = format.format(new Object[]{ remainingSleep });

            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        // Selected amount
        final var amountChoice = Objects.requireNonNull(event.getOption("amount"));
        final var amount = amountChoice.getAsInt();

        // Selected item
        final var itemChoice = Objects.requireNonNull(event.getOption("item"));
        final var item = this.itemManager.getItem(itemChoice.getAsString());

        final var newItemAmount = user.getItem(item.getName()) - amount;

        if (newItemAmount < 0) {
            final var msg = Responses.get("feedNotEnoughItems", locale);

            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        final var oldValue = user.getStat(item.getType());
        final var saturation = amount * item.getSaturation();

        if (oldValue + saturation > 100) {
            final var msg = Responses.get("feedTooMuchSaturation", locale);

            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        user.setItem(item.getName(), newItemAmount);
        user.setStat(item.getType(), oldValue + saturation);

        IDatabase.INSTANCE.updateUser(user);

        final var format = new MessageFormat(Responses.get(getKey(item), locale));
        final var msg = format.format(new Object[]{ amount, item.getName(), saturation });

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
                new OptionData(STRING, "item", "The item to feed", true)
                        .setDescriptionLocalization(DiscordLocale.GERMAN, "Das zu fütternde Item")
                        .addChoices(choices),
                new OptionData(INTEGER, "amount", "The amount of items", true)
                        .setDescriptionLocalization(DiscordLocale.GERMAN, "Die Anzahl an Items")
                        .setRequiredRange(1, 5)
        );

        return Commands.slash("feed", "Feeds your alpaca")
                       .setDescriptionLocalization(DiscordLocale.GERMAN, "Füttert dein Alpaka")
                       .addOptions(options);
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.USER;
    }

    private String getKey(final Item item) {
        return item.getType().equals("hunger")
                ? "feedHungerItem"
                : "feedThirstItem";
    }
}