package bot.commands.owner;

import bot.commands.types.OwnerCommand;
import bot.db.IDatabase;
import bot.models.User;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.List;
import java.util.Locale;

public class Set extends OwnerCommand {
    @Override
    protected void execute(final SlashCommandInteractionEvent event, final Locale locale) {
        // is the targeted user present in the database?
        final var userId = event.getOption("user-id").getAsUser().getIdLong();
        final var dbUser = IDatabase.INSTANCE.getUserById(userId);
        if (dbUser == null) {
            event.reply(Responses.getLocalizedResponse("general.error.targetNotInitialized", locale)).setEphemeral(true).queue();
            return;
        }

        final var newValue = event.getOption("new-value").getAsInt();

        // obtain response based on used subcommand
        final var response = switch (event.getSubcommandName()) {
            case "balance" -> modifyBalance(dbUser, newValue, locale);
            case "item" -> modifyItems(event, dbUser, newValue, locale);
            default -> Responses.getLocalizedResponse("set.error.unreachable", locale);
        };

        // update db
        IDatabase.INSTANCE.updateUser(dbUser);

        // reply to the user
        event.reply(response).setEphemeral(true).queue();
    }

    @Override
    public CommandData getCommandData() {
        final var itemChoices = List.of(
                new Command.Choice("salad", "salad"),
                new Command.Choice("taco", "taco"),
                new Command.Choice("steak", "steak"),
                new Command.Choice("water", "water"),
                new Command.Choice("lemonade", "lemonade"),
                new Command.Choice("cacao", "cacao")
        );

        final var itemOptions = List.of(
                new OptionData(OptionType.STRING, "item-name", "The item to modify", true)
                        .setDescriptionLocalization(DiscordLocale.GERMAN, "Das zu modifizierende Item")
                        .addChoices(itemChoices),
                new OptionData(OptionType.USER, "user-id", "The id of the user you want to modify", true)
                        .setDescriptionLocalization(DiscordLocale.GERMAN, "Die Id von dem zu modifizierenden Nutzer"),
                new OptionData(OptionType.INTEGER, "new-value", "The new item amount", true)
                        .setDescriptionLocalization(DiscordLocale.GERMAN, "Die neue Anzahl an Items")
        );

        final var balanceOptions = List.of(
                new OptionData(OptionType.USER, "user-id", "The id of the user you want to modify", true)
                        .setDescriptionLocalization(DiscordLocale.GERMAN, "Die Id von dem zu modifizierenden Nutzer"),
                new OptionData(OptionType.INTEGER, "new-value", "The new balance", true)
                        .setDescriptionLocalization(DiscordLocale.GERMAN, "Das neue Guthaben")
        );

        final var subCommands = List.of(
                new SubcommandData("balance", "Modify the users balance")
                        .addOptions(balanceOptions)
                        .setDescriptionLocalization(DiscordLocale.GERMAN, "Setzt ein neues Guthaben für den spezifizierten Nutzer"),
                new SubcommandData("item", "Modify the users items")
                        .addOptions(itemOptions)
                        .setDescriptionLocalization(DiscordLocale.GERMAN, "Setzt eine neue Anzahl an Items für den spezifizierten Nutzer")
        );

        return Commands.slash("set", "Sets the data for the specified user")
                       .setDescriptionLocalization(DiscordLocale.GERMAN, "Setzt die Daten für den spezifizierten Nutzer")
                       .addSubcommands(subCommands)
                       .setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.OWNER;
    }

    private String modifyBalance(final User dbUser, final int newValue, final Locale locale) {
        dbUser.getInventory().setCurrency(newValue);
        return Responses.getLocalizedResponse("set.modified.balance", locale, dbUser.getUserId(), newValue);
    }

    private String modifyItems(final SlashCommandInteractionEvent event, final User dbUser, final int newValue, final Locale locale) {
        final var item = event.getOption("item-name").getAsString();
        dbUser.getInventory().getItems().put(item, newValue);
        return Responses.getLocalizedResponse("set.modified.items", locale, item, dbUser.getUserId(), newValue);
    }
}