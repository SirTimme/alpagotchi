package bot.commands.dev;

import bot.commands.IInfoCommand;
import bot.db.IDatabase;
import bot.utils.Emote;
import bot.utils.ThreadFactory;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static bot.utils.Emote.REDCROSS;
import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Decrease implements IInfoCommand {
    private boolean running = false;
    private Future<?> result;
    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(new ThreadFactory());

    @Override
    public void execute(SlashCommandEvent event) {
        final String args = event.getOption("status").getAsString();
        if (running && args.equals("enable")) {
            event.reply(REDCROSS + " Decreasing is already enabled")
                 .setEphemeral(true)
                 .queue();
            return;
        }
        if (!running && args.equals("disable")) {
            event.reply(REDCROSS + " Decreasing is already disabled")
                 .setEphemeral(true)
                 .queue();
            return;
        }
        if (args.equals("enable")) {
            result = service.scheduleAtFixedRate(IDatabase.INSTANCE::decreaseValues, 2, 2, TimeUnit.HOURS);
            running = true;

            event.reply(Emote.GREENTICK + " Alpacas begin to lose stats").queue();
        } else {
            result.cancel(true);
            running = false;

            event.reply(REDCROSS + " Alpacas stop losing stats").queue();
        }
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("decrease", "Let the alpacas lose stats")
                .addOptions(
                        new OptionData(STRING, "status", "Status of decreasing", true)
                                .addChoices(
                                        new Command.Choice("enable", "enable"),
                                        new Command.Choice("disable", "disable")
                                )
                )
                .setDefaultEnabled(false);
    }
}
