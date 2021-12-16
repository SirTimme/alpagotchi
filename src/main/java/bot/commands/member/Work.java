package bot.commands.member;

import bot.commands.UserCommand;
import bot.db.IDatabase;
import bot.models.Entry;
import bot.utils.MessageService;
import bot.utils.Responses;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Work extends UserCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Work.class);
    private ArrayList<String> json;

    public Work() {
        try {
            final BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/data/messages.json"));
            final Type type = new TypeToken<List<String>>() {}.getType();
            this.json = new Gson().fromJson(reader, type);
        } catch (IOException error) {
            LOGGER.error(error.getMessage());
        }
    }

    @Override
    public void execute(final SlashCommandEvent event, final Locale locale, final Entry user) {
        final long sleep = user.getSleepAsMinutes();
        if (sleep > 0) {
            final MessageFormat msg = new MessageFormat(Responses.get("alpacaSleeping", locale));
            final String content = msg.format(new Object[]{ sleep });

            MessageService.queueReply(event, content, true);
            return;
        }

        final long work = user.getWorkAsMinutes();
        if (work > 0) {
            final MessageFormat msg = new MessageFormat(Responses.get("alpacaAlreadyWorked", locale));
            final String content = msg.format(new Object[]{ work });

            MessageService.queueReply(event, content, true);
            return;
        }

        final int energy = user.getEnergy();
        if (energy < 10) {
            event.reply("\uD83E\uDD71 Your alpaca is too tired to work, let it rest first with **/sleep**")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final int joy = user.getJoy();
        if (joy < 15) {
            event.reply(":pensive: Your alpaca is too sad to work, give him some love with **/pet**")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final String message = getRandomMessage();
        final int fluffies = (int) (Math.random() * 15 + 1);
        final int energyCost = (int) (Math.random() * 8 + 1);
        final int joyCost = (int) (Math.random() * 10 + 2);

        user.setCurrency(user.getCurrency() + fluffies);
        user.setEnergy(user.getEnergy() - energyCost);
        user.setJoy(user.getJoy() - joyCost);
        user.setWork(System.currentTimeMillis() + 1000L * 60 * 20);

        IDatabase.INSTANCE.updateUser(user);

        event.reply("⛏ " + message + " **Fluffies + " + fluffies + ", Energy - " + energyCost + ", Joy - " + joyCost + "**").queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("work", "Lets your alpaca work for fluffies");
    }

    private String getRandomMessage() {
        return this.json.get((int) (Math.random() * this.json.size()));
    }
}
