package bot.commands.member;

import bot.commands.UserSlashCommand;
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

public class Pet extends UserSlashCommand {
    private final List<String> spots = List.of("head", "tail", "leg", "neck", "back");

    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final User user) {
        // already max joy?
        final var joy = user.getAlpaca().getJoy();
        if (joy == 100) {
            final var msg = Responses.getLocalizedResponse("petJoyAlreadyMaximum", locale);
            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        // choose a random spot
        final var favouriteSpot = this.spots.get((int) (Math.random() * 5));

        // selected spot
        final var spot = event.getOption("spot").getAsString();

        // found the favourite Spot?
        final var isFavourite = spot.equals(favouriteSpot);

        // Update Db
        final var value = calculateJoy(joy, isFavourite);
        user.getAlpaca().setJoy(joy + value);

        // reply to the user
        final var format = new MessageFormat(Responses.getLocalizedResponse(getKey(isFavourite), locale));
        final var msg = format.format(new Object[]{ value });
        event.reply(msg).queue();
    }

    @Override
    public CommandData getCommandData() {
        final var choices = List.of(
                new Command.Choice("neck", "neck"),
                new Command.Choice("head", "head"),
                new Command.Choice("tail", "tail"),
                new Command.Choice("leg", "leg"),
                new Command.Choice("back", "back")
        );

        final var option = new OptionData(OptionType.STRING, "spot", "The spot to pet", true)
                .setDescriptionLocalization(DiscordLocale.GERMAN, "Die zu streichelnde Stelle")
                .addChoices(choices);

        return Commands.slash("pet", "Pets your alpaca to regain joy")
                       .setDescriptionLocalization(DiscordLocale.GERMAN, "Streichelt dein Alpaka, um Freude zu regenerieren")
                       .addOptions(option);
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.USER;
    }

    private String getKey(final boolean isFavourite) {
        return isFavourite ? "petFavouriteSpot" : "petNormalSpot";
    }

    private int calculateJoy(final int joy, final boolean isFavourite) {
        final int value = isFavourite ? (int) (Math.random() * 13 + 5) : (int) (Math.random() * 9 + 3);
        return joy + value > 100 ? 100 - joy : value;
    }
}