package bot.commands.member;

import bot.commands.UserCommand;
import bot.db.IDatabase;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Pet extends UserCommand {
    private final List<String> spots = Arrays.asList("head", "tail", "leg", "neck", "back");

    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final Entry user) {
        final var joy = user.getJoy();
        if (joy == 100) {
            final var format = new MessageFormat(Responses.get("petJoyAlreadyMaximum", locale));
            final var msg = format.format(new Object[]{});

            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        final var favouriteSpot = this.spots.get((int) (Math.random() * 5));
        final var spot = event.getOption("spot").getAsString();

        final var isFavourite = spot.equals(favouriteSpot);

        final var value = calculateJoy(joy, isFavourite);

        user.setJoy(joy + value);
        IDatabase.INSTANCE.updateUser(user);

        final var format = new MessageFormat(Responses.get(getKey(isFavourite), locale));
        final var msg = format.format(new Object[] { joy });

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

        final var option = new OptionData(STRING, "spot", "The spot to pet", true)
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
        return isFavourite
                ? "petFavouriteSpot"
                : "petNormalSpot";
    }

    private int calculateJoy(final int joy, final boolean isFavourite) {
        final int value = isFavourite ? (int) (Math.random() * 13 + 5) : (int) (Math.random() * 9 + 3);

        return joy + value > 100 ? 100 - joy : value;
    }
}