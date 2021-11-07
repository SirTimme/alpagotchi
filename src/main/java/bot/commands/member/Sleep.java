package bot.commands.member;

import bot.commands.IDynamicUserCommand;
import bot.models.Entry;
import bot.utils.Emote;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;

public class Sleep implements IDynamicUserCommand {
    @Override
    public Entry execute(SlashCommandEvent event, Entry user) {
        int energy = user.getEnergy();

        if (energy == 100) {
            event.reply(Emote.REDCROSS + " The energy of your alpaca is already at the maximum")
                 .setEphemeral(true)
                 .queue();
            return null;
        }

        final int duration = (int) event.getOption("duration").getAsLong();
        if (duration < 1 || duration > 100) {
            event.reply(Emote.REDCROSS + " Please enter a number between 1 - 100")
                 .setEphemeral(true)
                 .queue();
            return null;
        }

        energy = energy + duration > 100 ? 100 - energy : duration;

        final long cooldown = System.currentTimeMillis() + 1000L * 60 * energy;

        user.setEnergy(energy);
        user.setSleep(cooldown);

        event.reply("\uD83D\uDCA4 Your alpaca goes to bed for **" + energy + "** minutes and rests well **Energy + " + energy + "**")
             .queue();

        return user;
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("sleep", "Lets your alpaca sleep for the specified duration to regain energy")
                .addOptions(
                        new OptionData(INTEGER, "duration", "The duration in minutes", true)
                );
    }
}
