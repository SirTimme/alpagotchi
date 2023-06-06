package bot.commands.member;

import bot.commands.MutableUserCommand;
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
import java.util.Objects;

public class Gift extends MutableUserCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final User user) {
        // Selected user
        final var userChoice = Objects.requireNonNull(event.getOption("user"));
        final var targetDiscordUser = userChoice.getAsUser();

        // You can't gift items to yourself
        if (targetDiscordUser.getIdLong() == user.getMemberID()) {
            final var msg = Responses.getLocalizedResponse("giftCantGiftYourself", locale);

            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        // Is the target user initialized?
        // final var targetDbUser = IDatabase.INSTANCE.getUserById(targetDiscordUser.getIdLong());
        final var targetDbUser = new Entry(23L);
        if (targetDbUser == null) {
            final var msg = Responses.getLocalizedResponse("giftTargetNotInitialized", locale);

            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        // Selected amount
        final var amountChoice = Objects.requireNonNull(event.getOption("amount"));
        final var amount = amountChoice.getAsInt();

        // Selected item
        final var itemChoice = Objects.requireNonNull(event.getOption("item"));
        final var item = itemChoice.getAsString();

        // You can only gift as many items as you possess
        if (user.getItem(item) - amount < 0) {
            final var msg = Responses.getLocalizedResponse("feedNotEnoughItems", locale);

            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        // Update Db
        user.setItem(item, user.getItem(item) - amount);
        targetDbUser.setItem(item, targetDbUser.getItem(item) + amount);
        IDatabase.INSTANCE.updateUser(targetDbUser);
        IDatabase.INSTANCE.updateUser(user);

        final var format = new MessageFormat(Responses.getLocalizedResponse("giftSuccessful", locale));
        final var msg = format.format(new Object[]{ amount, item, targetDiscordUser.getName() });

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