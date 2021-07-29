package Bot.Command.Member;

import Bot.Command.ISlashCommand;
import Bot.Database.IDatabase;
import Bot.Models.User;
import Bot.Utils.Emote;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;

public class Sleep implements ISlashCommand {
    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        User user = IDatabase.INSTANCE.getUser(authorID);

        if (user == null) {
            event.reply(Emote.REDCROSS + " You don't own an alpaca, use **/init** first")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        int energy = user.getAlpaca().getEnergy();

        if (energy == 100) {
            event.reply(Emote.REDCROSS + " The energy of your alpaca is already at the maximum")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final int duration = (int) event.getOption("duration").getAsLong();

        if (duration < 1 || duration > 100) {
            event.reply(Emote.REDCROSS + " Please enter a number between 1 - 100")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        energy = energy + duration > 100 ? 100 - energy : duration;
        final long cooldown = System.currentTimeMillis() + 1000L * 60 * energy;

        user.getAlpaca().setEnergy(energy);
        user.getCooldown().setSleep(cooldown);

        IDatabase.INSTANCE.setUser(authorID, user);

        event.reply("\uD83D\uDCA4 Your alpaca goes to bed for **" + energy + "** minutes and rests well **Energy + " + energy + "**")
             .queue();

    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("sleep", "Lets your alpaca sleep for the specified duration to regain energy")
                .addOptions(
                        new OptionData(INTEGER, "duration", "The duration in minutes", true)
                );
    }
}
