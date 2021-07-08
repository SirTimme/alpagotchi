package Bot.Command.Member;

import Bot.Command.ISlashCommand;
import Bot.Database.IDatabase;
import Bot.Models.Entry;
import Bot.Utils.Emote;
import Bot.Utils.Language;
import Bot.Utils.Stat;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Work implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Work.class);
    private final List<String> json = new ArrayList<>();

    public Work() {
        try {
            final File file = new File("src/main/resources/data/Messages.json");
            final Path filePath = Path.of(file.getPath());
            final String content = Files.readString(filePath);
            final JSONArray messages = new JSONArray(content);

            for (int i = 0; i < messages.length(); i++) {
                this.json.add(messages.getString(i));
            }
        } catch (IOException error) {
            LOGGER.error(error.getMessage());
        }
    }

    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        final Entry entry = IDatabase.INSTANCE.getEntry(authorID);

        if (entry == null) {
            event.reply(Emote.REDCROSS + " You don't own an alpaca, use **/init** first")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        long sleep = TimeUnit.MILLISECONDS.toMinutes(entry.getCooldowns().getSleep() - System.currentTimeMillis());

        if (sleep > 0) {
            event.reply(Emote.REDCROSS + " Your alpaca sleeps, it'll wake up in **" + Language.handle(sleep, "minute") + "**")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        long work = TimeUnit.MILLISECONDS.toMinutes(entry.getCooldowns().getWork() - System.currentTimeMillis());

        if (work > 0) {
            event.reply(Emote.REDCROSS + " Your alpaca has to rest **" + Language.handle(work, "minute") + "** to work again")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final int energy = entry.getAlpaca().getEnergy();

        if (energy < 10) {
            event.reply("\uD83E\uDD71 Your alpaca is too tired to work, let it rest first with **/sleep**")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final int joy = entry.getAlpaca().getJoy();

        if (joy < 15) {
            event.reply(":pensive: Your alpaca is too sad to work, give him some love with **/pet**")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final int fluffies = (int) (Math.random() * 15 + 1);
        IDatabase.INSTANCE.setEntry(authorID, Stat.CURRENCY, fluffies);

        final int energyCost = (int) (Math.random() * 8 + 1);
        IDatabase.INSTANCE.setEntry(authorID, Stat.ENERGY, -energyCost);

        final int joyCost = (int) (Math.random() * 10 + 2);
        IDatabase.INSTANCE.setEntry(authorID, Stat.JOY, -joyCost);

        final long cooldown = System.currentTimeMillis() + 1000L * 60 * 20;
        IDatabase.INSTANCE.setEntry(authorID, Stat.WORK, cooldown);

        final String message = getRandomMessage();

        event.reply("⛏ " + message + " **Fluffies + " + fluffies + ", Energy - " + energyCost + ", Joy - " + joyCost + "**").queue();
    }

    private String getRandomMessage() {
        return json.get((int) (Math.random() * json.size()));
    }
}
