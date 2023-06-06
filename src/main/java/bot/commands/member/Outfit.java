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

public class Outfit extends MutableUserCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final User user) {
        // selected outfit
        final var outfit = event.getOption("outfit").getAsString();

        // update outfit
        user.getAlpaca().setOutfit(outfit);

        // reply to the user
        final var format = new MessageFormat(Responses.getLocalizedResponse("outfit", locale));
        final var msg = format.format(new Object[]{ outfit });
        event.reply(msg).queue();
    }

    @Override
    public CommandData getCommandData() {
        final var choices = List.of(
                new Command.Choice("default", "default"),
                new Command.Choice("gentleman", "gentleman"),
                new Command.Choice("lady", "lady")
        );

        final var option = new OptionData(OptionType.STRING, "outfit", "The new outfit", true)
                .setDescriptionLocalization(DiscordLocale.GERMAN, "Das neue Outfit")
                .addChoices(choices);

        return Commands.slash("outfit", "Changes the outfit of your alpaca")
                       .setDescriptionLocalization(DiscordLocale.GERMAN, "Gibt deinem Alpaka ein neues Outfit")
                       .addOptions(option);
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.USER;
    }
}