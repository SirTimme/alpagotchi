package bot.commands.member;

import bot.commands.IDynamicUserCommand;
import bot.models.Entry;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Arrays;
import java.util.List;

import static bot.utils.Emote.REDCROSS;
import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Pet implements IDynamicUserCommand {
    private final List<String> spots = Arrays.asList("head", "tail", "leg", "neck", "back");

    @Override
    public Entry execute(SlashCommandEvent event, Entry user) {
        final int joy = user.getJoy();
        if (joy == 100) {
            event.reply(REDCROSS + " The joy of your alpaca is already at the maximum")
                 .setEphemeral(true)
                 .queue();
            return null;
        }

        final String favouriteSpot = spots.get((int) (Math.random() * 5));
        final String spot = event.getOption("spot").getAsString();

        int newJoy;
        if (spot.equals(favouriteSpot)) {
            final int randomJoy = (int) (Math.random() * 13 + 5);
            newJoy = randomJoy + joy > 100 ? 100 - joy : randomJoy;

            event.reply("\uD83E\uDD99 You found the favourite spot of your alpaca **Joy + " + newJoy + "**").queue();
        } else {
            final int randomJoy = (int) (Math.random() * 9 + 3);
            newJoy = randomJoy + joy > 100 ? 100 - joy : randomJoy;

            event.reply("\uD83E\uDD99 Your alpaca enjoyed the petting, but it wasn't his favourite spot **Joy + " + newJoy + "**").queue();
        }

        user.setJoy(newJoy);

        return user;
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
}
