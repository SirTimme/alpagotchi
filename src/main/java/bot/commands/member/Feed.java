package bot.commands.member;

import bot.commands.MutableUserCommand;
import bot.db.IDatabase;
import bot.models.User;
import bot.shop.IConsumable;
import bot.shop.ItemManager;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Feed extends MutableUserCommand {
    private final ItemManager itemManager;

    public Feed(final ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final User user) {
        // current sleep cooldown
        final var remainingSleep = user.getCooldown().getSleep();

        // alpaca currently sleeping?
        if (remainingSleep > 0) {
            final var format = new MessageFormat(Responses.getLocalizedResponse("sleepCurrentlySleeping", locale));
            final var msg = format.format(new Object[]{ remainingSleep });
            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        // selected amount
        final var amount = event.getOption("amount").getAsInt();

        // selected item
        final var item = this.itemManager.getItem(event.getOption("item").getAsString());

        // remove the fed items
        final var newItemAmount = user.getInventory().getItems().get(item.getName()) - amount;

        // trying to feed items you don't have?
        if (newItemAmount < 0) {
            final var msg = Responses.getLocalizedResponse("feedNotEnoughItems", locale);
            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        // old saturation value
        final var oldValue = user.getSaturation(item.getType());

        // new saturation with the fed item(s)
        final var saturation = amount * item.getSaturation();

        // trying to overfeed your alpaca?
        if (oldValue + saturation > 100) {
            final var msg = Responses.getLocalizedResponse("feedTooMuchSaturation", locale);
            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        // update db
        user.getInventory().setItem(item.getName(), newItemAmount);
        user.setSaturation(item.getType(), oldValue + saturation);

        // reply to the user
        final var format = new MessageFormat(Responses.getLocalizedResponse(getKey(item), locale));
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
                new OptionData(OptionType.STRING, "item", "The item to feed", true)
                        .setDescriptionLocalization(DiscordLocale.GERMAN, "Das zu fütternde Item")
                        .addChoices(choices),
                new OptionData(OptionType.INTEGER, "amount", "The amount of items", true)
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

    private String getKey(final IConsumable item) {
        return item.getType().equals("hunger")
                ? "feedHungerItem"
                : "feedThirstItem";
    }
}