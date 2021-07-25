package Bot.Command.Member;

import Bot.Command.ISlashCommand;
import Bot.Database.IDatabase;
import Bot.Models.User;
import Bot.Utils.Emote;
import Bot.Utils.Language;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Work implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Work.class);
    private final List<String> json = new ArrayList<>();

    public Work() {
        try {
            final File file = new File("src/main/resources/Data/Messages.json");
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
        final User user = IDatabase.INSTANCE.getUser(authorID);

        if (user == null) {
            event.reply(Emote.REDCROSS + " You don't own an alpaca, use **/init** first")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        long sleep = user.getCooldown().getSleep();
        if (sleep > 0) {
            event.reply(Emote.REDCROSS + " Your alpaca sleeps, it'll wake up in **" + Language.handle(sleep, "minute") + "**")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        long work = user.getCooldown().getWork();
        if (work > 0) {
            event.reply(Emote.REDCROSS + " Your alpaca has to rest **" + Language.handle(work, "minute") + "** to work again")
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

        IDatabase.INSTANCE.setUser(authorID, user);

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
