package bot.commands.member;

import bot.commands.UserSlashCommand;
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

import java.util.List;
import java.util.Locale;

public class Feed extends UserSlashCommand {
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
            event.reply(Responses.getLocalizedResponse("work.error.currentlySleeping", locale, remainingSleep)).setEphemeral(true).queue();
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
            event.reply(Responses.getLocalizedResponse("general.error.notEnoughItems", locale)).setEphemeral(true).queue();
            return;
        }

        // old saturation value
        final var oldValue = retrieveItemSaturation(user, item);

        // new saturation with the fed item(s)
        final var saturation = amount * item.getSaturation();

        // trying to overfeed your alpaca?
        if (oldValue + saturation > 100) {
            event.reply(Responses.getLocalizedResponse("feed.error.tooMuchSaturation", locale)).setEphemeral(true).queue();
            return;
        }

        // update db
        user.getInventory().getItems().put(item.getName(), newItemAmount);

        if (item.getType().equals("food")) {
            user.getAlpaca().setHunger(oldValue + saturation);
        } else {
            user.getAlpaca().setThirst(oldValue + saturation);
        }

        IDatabase.INSTANCE.updateUser(user);

        // reply to the user
        event.reply(Responses.getLocalizedResponse(getKey(item), locale, amount, item.getName(), saturation)).queue();
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
        return item.getType().equals("food") ? "feed.feedItem.food" : "feed.feedItem.drink";
    }

    private int retrieveItemSaturation(final User user, final IConsumable item) {
        return item.getType().equals("food") ? user.getAlpaca().getHunger() : user.getAlpaca().getThirst();
    }
}