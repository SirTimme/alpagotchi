package bot.commands.member;

import bot.commands.interfaces.IDynamicUserCommand;
import bot.models.Entry;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.MessageFormat;
import java.util.Locale;

import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;

public class Sleep implements IDynamicUserCommand {
    @Override
    public Entry execute(final SlashCommandEvent event, final Entry user, final Locale locale) {
        final int energy = user.getEnergy();
        if (energy == 100) {
            final MessageFormat msg = new MessageFormat(Responses.get("joyAtMaximum", locale));
            MessageService.reply(event, msg, true);
            return null;
        }

        final int duration = (int) event.getOption("duration").getAsLong();
        if (duration < 1 || duration > 100) {
            event.reply("Please enter a number between 1 - 100")
                 .setEphemeral(true)
                 .queue();
            return null;
        }

        final int newEnergy = energy + duration > 100 ? 100 - energy : duration;

        user.setEnergy(energy + newEnergy);
        user.setSleep(System.currentTimeMillis() + 1000L * 60 * newEnergy);

        event.reply("\uD83D\uDCA4 Your alpaca goes to bed for **" + newEnergy + "** minutes and rests well **Energy + " + newEnergy + "**")
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
