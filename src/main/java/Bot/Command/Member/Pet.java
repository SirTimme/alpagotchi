package Bot.Command.Member;

import Bot.Command.ISlashCommand;
import Bot.Database.IDatabase;
import Bot.Models.User;
import Bot.Utils.Emote;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Arrays;
import java.util.List;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Pet implements ISlashCommand {
    private final List<String> spots = Arrays.asList("head", "tail", "leg", "neck", "back");

    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        User user = IDatabase.INSTANCE.getUser(authorID);

        if (user == null) {
            event.reply(Emote.REDCROSS + " You don't own an alpaca, use **/init** first")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final int joy = user.getAlpaca().getJoy();

        if (joy == 100) {
            event.reply(Emote.REDCROSS + " The joy of your alpaca is already at the maximum")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final String favouriteSpot = spots.get((int) (Math.random() * 5));
        final String spot = event.getOption("spot").getAsString();

        if (spot.equals(favouriteSpot)) {
            int newJoy = (int) (Math.random() * 13 + 5);
            newJoy = newJoy + joy > 100 ? 100 - joy : newJoy;

            user.getAlpaca().setJoy(newJoy);
            IDatabase.INSTANCE.setUser(authorID, user);

            event.reply("\uD83E\uDD99 You found the favourite spot of your alpaca **Joy + " + newJoy + "**").queue();
        } else {
            int newJoy = (int) (Math.random() * 9 + 3);
            newJoy = newJoy + joy > 100 ? 100 - joy : newJoy;

            user.getAlpaca().setJoy(newJoy);

            IDatabase.INSTANCE.setUser(authorID, user);

            event.reply("\uD83E\uDD99 Your alpaca enjoyed the petting, but it wasn't his favourite spot **Joy + " + newJoy + "**").queue();
        }
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
