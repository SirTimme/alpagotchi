package bot.commands.member;

import bot.commands.SlashCommand;
import bot.db.IDatabase;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.MessageFormat;
import java.util.Locale;

import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;

public class Sleep extends SlashCommand {
    @Override
    public void execute(final SlashCommandEvent event, final Locale locale, final Entry user) {
        final int energy = user.getEnergy();
        if (energy == 100) {
            MessageService.queueReply(event, new MessageFormat(Responses.get("joyAtMaximum", locale)), true);
            return;
        }

        final int duration = (int) event.getOption("duration").getAsLong();
        if (duration < 1 || duration > 100) {
            MessageService.queueReply(event, new MessageFormat("nonValidNumber", locale), true);
            return;
        }

        final int newEnergy = energy + duration > 100 ? 100 - energy : duration;

        user.setEnergy(energy + newEnergy);
        user.setSleep(System.currentTimeMillis() + 1000L * 60 * newEnergy);
        IDatabase.INSTANCE.updateUser(user);

        event.reply("\uD83D\uDCA4 Your alpaca goes to bed for **" + newEnergy + "** minutes and rests well **Energy + " + newEnergy + "**")
             .queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("sleep", "Lets your alpaca sleep for the specified duration to regain energy")
                .addOptions(new OptionData(INTEGER, "duration", "The duration in minutes", true));
    }

    @Override
    protected CommandType getCommandType() {
        return CommandType.USER;
    }
}
