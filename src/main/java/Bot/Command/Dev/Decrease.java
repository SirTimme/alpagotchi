package Bot.Command.Dev;

import Bot.Command.ISlashCommand;
import Bot.Database.IDatabase;
import Bot.Utils.Emote;
import Bot.Utils.ThreadFactory;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Decrease implements ISlashCommand {
    private boolean running = false;
    private Future<?> result;
    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(new ThreadFactory());

    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        final String args = event.getOption("status").getAsString();

        if (running && args.equals("enable")) {
            event.reply(Emote.REDCROSS + " Decreasing is already enabled")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        if (!running && args.equals("disable")) {
            event.reply(Emote.REDCROSS + " Decreasing is already disabled")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        if (args.equals("enable")) {
            result = service.scheduleAtFixedRate(IDatabase.INSTANCE::decreaseValues, 2, 2, TimeUnit.HOURS);
            running = true;

            event.reply(Emote.GREENTICK + " Alpacas begin to lose stats").queue();
        }
        else {
            result.cancel(true);
            running = false;

            event.reply(Emote.REDCROSS + " Alpacas stop losing stats").queue();
        }
    }
}
