package bot.commands.dev;

import bot.commands.InfoSlashCommand;
import bot.db.IDatabase;
import bot.utils.CommandType;
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

public class Set extends InfoSlashCommand {
    @Override
    protected void execute(final SlashCommandInteractionEvent event, final Locale locale) {
        event.reply("Received modifying request").queue();
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
                new OptionData(OptionType.INTEGER, "new-amount", "The new item amount", true)
                        .setDescriptionLocalization(DiscordLocale.GERMAN, "Die neue Anzahl an Items")
        );

        final var balanceOptions = List.of(
                new OptionData(OptionType.USER, "user-id", "The id of the user you want to modify", true)
                        .setDescriptionLocalization(DiscordLocale.GERMAN, "Die Id von dem zu modifizierenden Nutzer"),
                new OptionData(OptionType.INTEGER, "new-balance", "The new balance", true)
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
        return CommandType.DEV;
    }
}