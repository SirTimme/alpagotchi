package Bot.Command.Member;

import Bot.Database.IDatabase;
import Bot.Command.IUserCommand;
import Bot.Models.DBUser;
import Bot.Utils.Language;
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
import java.util.ArrayList;
import java.util.List;

import static Bot.Utils.Emote.REDCROSS;

public class Work implements IUserCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Work.class);
    private ArrayList<String> json;

    public Work() {
        try {
            final BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/data/messages.json"));
            final Type type = new TypeToken<List<String>>() {}.getType();

            json = new Gson().fromJson(reader, type);
        } catch (IOException error) {
            LOGGER.error(error.getMessage());
        }
    }

    @Override
    public void execute(SlashCommandEvent event, DBUser user) {
        final long sleep = user.getCooldown().getSleep();
        if (sleep > 0) {
            event.reply(REDCROSS + " Your alpaca sleeps, it'll wake up in **" + sleep + " " + Language.handle(sleep, "minute", "minutes") + "**")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final long work = user.getCooldown().getWork();
        if (work > 0) {
            event.reply(REDCROSS + " Your alpaca has to rest **" + work + " " + Language.handle(work, "minute", "minutes") + "** to work again")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final int energy = user.getAlpaca().getEnergy();
        if (energy < 10) {
            event.reply("\uD83E\uDD71 Your alpaca is too tired to work, let it rest first with **/sleep**")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final int joy = user.getAlpaca().getJoy();
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
        final long cooldown = System.currentTimeMillis() + 1000L * 60 * 20;

        user.getInventory().setCurrency(fluffies);
        user.getAlpaca().setEnergy(-energyCost);
        user.getAlpaca().setJoy(-joyCost);
        user.getCooldown().setWork(cooldown);
        IDatabase.INSTANCE.setUser(user.getId(), user);

        event.reply("⛏ " + message + " **Fluffies + " + fluffies + ", Energy - " + energyCost + ", Joy - " + joyCost + "**")
             .queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("work", "Lets your alpaca work for fluffies");
    }

    private String getRandomMessage() {
        return json.get((int) (Math.random() * json.size()));
    }
}
