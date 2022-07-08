package bot.commands.member;

import bot.commands.ISlashCommand;
import bot.db.IDatabase;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.MessageFormat;
import java.util.Locale;

import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;

public class Sleep implements ISlashCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final Entry user) {
        final int energy = user.getEnergy();
        if (energy == 100) {
            final var format = new MessageFormat(Responses.get("joyAtMaximum", locale));
            final var msg = format.format(new Object[]{});

            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        final int duration = event.getOption("duration").getAsInt();
        if (duration < 1 || duration > 100) {
            final var format = new MessageFormat("nonValidNumber", locale);
            final var msg = format.format(new Object[]{});

            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        final var newEnergy = energy + duration > 100 ? 100 - energy : duration;

        user.setEnergy(energy + newEnergy);
        user.setSleep(System.currentTimeMillis() + 1000L * 60 * newEnergy);
        IDatabase.INSTANCE.updateUser(user);

        final var format = new MessageFormat(Responses.get("sleep", locale));
        final var msg = format.format(new Object[]{ newEnergy });

        event.reply(msg).queue();
    }

    @Override
    public CommandData getCommandData() {
        final var option = new OptionData(INTEGER, "duration", "The duration in minutes", true);

        return Commands.slash("sleep", "Lets your alpaca sleep for the specified duration to regain energy")
                       .addOptions(option);
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.USER;
    }
}