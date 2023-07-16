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
            final var msg = Responses.getLocalizedResponse("giftCantGiftYourself", locale);
            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        // Is the target user initialized?
        final var targetDbUser = IDatabase.INSTANCE.getUserById(targetDiscordUser.getIdLong());
        if (targetDbUser == null) {
            final var msg = Responses.getLocalizedResponse("giftTargetNotInitialized", locale);
            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        // selected amount
        final var amount = event.getOption("amount").getAsInt();

        // selected item
        final var item = event.getOption("item").getAsString();

        // you can only gift as many items as you possess
        if (user.getInventory().getAmount(item) - amount < 0) {
            final var msg = Responses.getLocalizedResponse("feedNotEnoughItems", locale);
            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        // Modify values accordingly
        user.getInventory().setItem(item, user.getInventory().getAmount(item) - amount);
        targetDbUser.getInventory().setItem(item, targetDbUser.getInventory().getAmount(item) + amount);

        // reply to the user
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