package bot.commands.member;

import bot.commands.ISlashCommand;
import bot.db.IDatabase;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Pet implements ISlashCommand {
    private final List<String> spots = Arrays.asList("head", "tail", "leg", "neck", "back");

    @Override
    public void execute(final SlashCommandEvent event, final Locale locale, final Entry user) {
        final int joy = user.getJoy();
        if (joy == 100) {
            MessageService.queueReply(event, new MessageFormat(Responses.get("joyAtMaximum", locale)), true);
            return;
        }

        final String favouriteSpot = this.spots.get((int) (Math.random() * 5));
        final String spot = event.getOption("spot").getAsString();
        final boolean isFavourite = spot.equals(favouriteSpot);

        final int value = calculateJoy(joy, isFavourite);
        final String msg = getMessage(value, isFavourite, locale);

        user.setJoy(joy + value);
        IDatabase.INSTANCE.updateUser(user);

        MessageService.queueReply(event, msg, false);
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("pet", "Pets your alpaca to gain joy").addOptions(
                new OptionData(STRING, "spot", "The spot where you want to pet your alpaca", true)
                        .addChoices(
                                new Command.Choice("neck", "neck"),
                                new Command.Choice("head", "head"),
                                new Command.Choice("tail", "tail"),
                                new Command.Choice("leg", "leg"),
                                new Command.Choice("back", "back")
                        )
        );
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.USER;
    }

    private String getMessage(final int joy, final boolean isFavourite, final Locale locale) {
        final String key = isFavourite ? "favouriteSpot" : "normalSpot";
        return new MessageFormat(Responses.get(key, locale)).format(new Object[]{ joy });
    }

    private int calculateJoy(final int joy, final boolean isFavourite) {
        final int value = isFavourite ? (int) (Math.random() * 13 + 5) : (int) (Math.random() * 9 + 3);
        return joy + value > 100 ? 100 - joy : value;
    }
}
