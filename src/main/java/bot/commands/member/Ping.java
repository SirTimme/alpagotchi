package bot.commands.member;

import bot.commands.SlashCommand;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.text.MessageFormat;
import java.util.Locale;

public class Ping extends SlashCommand {
    @Override
    public void execute(final SlashCommandEvent event, final Locale locale, final Entry user) {
        event.getJDA().getRestPing().queue(ping -> {
            final MessageFormat msg = new MessageFormat(Responses.get("ping", locale));
            final String content = msg.format(new Object[]{ ping });

            MessageService.queueReply(event, content, false);
        });
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("ping", "Displays the current latency of Alpagotchi");
    }

    @Override
    protected CommandType getCommandType() {
        return CommandType.INFO;
    }
}
