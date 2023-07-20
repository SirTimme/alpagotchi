package bot.commands.member;

import bot.commands.UserSlashCommand;
import bot.db.IDatabase;
import bot.models.User;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.text.MessageFormat;
import java.util.Locale;

public class Sleep extends UserSlashCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final User user) {
        // already max energy?
        final var energy = user.getAlpaca().getEnergy();
        if (energy == 100) {
            event.reply(Responses.getLocalizedResponse("petJoyAlreadyMaximum", locale)).setEphemeral(true).queue();
            return;
        }

        // Selected duration
        final var duration = event.getOption("duration").getAsInt();

        final var newEnergy = energy + duration > 100 ? 100 - energy : duration;

        // Update data
        user.getAlpaca().setEnergy(energy + newEnergy);
        user.getCooldown().setSleep(System.currentTimeMillis() + 1000L * 60 * newEnergy);
        IDatabase.INSTANCE.updateUser(user);

        // reply to the user
        event.reply(Responses.getLocalizedResponse("sleep", locale, newEnergy)).queue();
    }

    @Override
    public CommandData getCommandData() {
        final var option = new OptionData(OptionType.INTEGER, "duration", "The duration in minutes", true)
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