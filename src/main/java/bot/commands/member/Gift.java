package bot.commands.member;

import bot.commands.UserSlashCommand;
import bot.db.IDatabase;
import bot.models.User;
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

public class Gift extends UserSlashCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final User user) {
        // selected user
        final var targetDiscordUser = event.getOption("user").getAsUser();

        // you can't gift items to yourself
        if (targetDiscordUser.getIdLong() == user.getUserId()) {
            event.reply(Responses.getLocalizedResponse("giftCantGiftYourself", locale)).setEphemeral(true).queue();
            return;
        }

        // Is the target user initialized?
        final var targetUser = IDatabase.INSTANCE.getUserById(targetDiscordUser.getIdLong());
        if (targetUser == null) {
            event.reply(Responses.getLocalizedResponse("giftTargetNotInitialized", locale)).setEphemeral(true).queue();
            return;
        }

        // selected amount
        final var amount = event.getOption("amount").getAsInt();

        // selected item
        final var item = event.getOption("item").getAsString();

        // you can only gift as many items as you possess
        if (user.getInventory().getItems().get(item) - amount < 0) {
            event.reply(Responses.getLocalizedResponse("feedNotEnoughItems", locale)).setEphemeral(true).queue();
            return;
        }

        final var newSourceItemAmount = user.getInventory().getItems().get(item) - amount;
        final var newTargetItemAmount = targetUser.getInventory().getItems().get(item) + amount;

        // Modify values accordingly
        user.getInventory().getItems().put(item, newSourceItemAmount);
        targetUser.getInventory().getItems().put(item, newTargetItemAmount);

        IDatabase.INSTANCE.updateUser(user);
        IDatabase.INSTANCE.updateUser(targetUser);

        // reply to the user
        event.reply(Responses.getLocalizedResponse("giftSuccessful", locale, amount, item, targetDiscordUser.getName())).queue();
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
                new OptionData(OptionType.USER, "user", "The user you want to gift", true)
                        .setDescriptionLocalization(DiscordLocale.GERMAN, "Der zu beschenkende User"),
                new OptionData(OptionType.STRING, "item", "The item to gift", true)
                        .setDescriptionLocalization(DiscordLocale.GERMAN, "Das zu schenkende Item")
                        .addChoices(choices),
                new OptionData(OptionType.INTEGER, "amount", "The amount of gifted items", true)
                        .setDescriptionLocalization(DiscordLocale.GERMAN, "Die Anzahl an Items")
                        .setRequiredRange(1, 5)
        );

        return Commands.slash("gift", "Gifts another user")
                       .setDescriptionLocalization(DiscordLocale.GERMAN, "Beschenkt einen anderen Nutzer")
                       .addOptions(options)
                       .setGuildOnly(true);
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.USER;
    }
}