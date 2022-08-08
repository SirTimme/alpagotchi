package bot.commands.member;

import bot.commands.UserCommand;
import bot.db.IDatabase;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;

import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;

public class Sleep extends UserCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final Entry user) {
        // Already max energy?
        final var energy = user.getEnergy();
        if (energy == 100) {
            final var msg = Responses.get("petJoyAlreadyMaximum", locale);

            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        // Selected duration
        final var durationChoice = Objects.requireNonNull(event.getOption("duration"));
        final var duration = durationChoice.getAsInt();

        // Update db
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
        final var option = new OptionData(INTEGER, "duration", "The duration in minutes", true)
                .setRequiredRange(1, 100)
                .setDescriptionLocalization(DiscordLocale.GERMAN, "Die Dauer in Minuten");

        return Commands.slash("sleep", "Let your alpaca sleep to regain energy")
                       .setDescriptionLocalization(DiscordLocale.GERMAN, "Lässt dein Alpaka schlafen, um Energie zu regenerieren")
                       .addOptions(option);
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.USER;
    }
}