package bot.commands.member;

import bot.commands.UserCommand;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.text.MessageFormat;
import java.util.Locale;

public class Balance extends UserCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final Entry user) {
        final var format = new MessageFormat(Responses.get("currentBalance", locale));
        final var msg = format.format(new Object[]{user.getCurrency()});

        event.reply(msg).queue();
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("balance", "Shows your fluffy balance");
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.USER;
    }
}