package Bot.Command;

import Bot.Command.Dev.*;
import Bot.Command.Dev.Count;
import Bot.Command.Member.*;
import Bot.Database.IDatabase;
import Bot.Models.DBUser;
import Bot.Shop.ItemManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import static Bot.Utils.Emote.REDCROSS;

public class SlashCommandManager {
    private final static Logger LOGGER = LoggerFactory.getLogger(SlashCommandManager.class);
    private final Map<String, ISlashCommand> commands = new TreeMap<>();
    private Map<String, JsonObject> commandInfo;

    public SlashCommandManager() {
        try {
            final BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/data/commands.json"));
            final Type type = new TypeToken<Map<String, JsonObject>>() {}.getType();

            commandInfo = new Gson().fromJson(reader, type);
        } catch (IOException error) {
            LOGGER.error(error.getMessage());
        }
        final ItemManager itemMan = new ItemManager();

        commands.put("ping", new Ping());
        commands.put("init", new Init());
        commands.put("balance", new Balance());
        commands.put("buy", new Buy(itemMan));
        commands.put("count", new Count());
        commands.put("delete", new Delete());
        commands.put("feed", new Feed(itemMan));
        commands.put("gift", new Gift(itemMan));
        commands.put("image", new Image());
        commands.put("work", new Work());
        commands.put("shutdown", new Shutdown());
        commands.put("decrease", new Decrease());
        commands.put("myalpaca", new MyAlpaca());
        commands.put("nick", new Nick());
        commands.put("help", new Help(this));
        commands.put("sleep", new Sleep());
        commands.put("outfit", new Outfit());
        commands.put("pet", new Pet());
        commands.put("inventory", new Inventory(itemMan));
        commands.put("shop", new Shop(itemMan));
        commands.put("update", new Update(this));
    }

    public void handle(SlashCommandEvent event) {
        final String eventName = event.getName();

        if (isUserRequired(eventName)) {
            final DBUser user = IDatabase.INSTANCE.getUser(event.getUser().getIdLong());

            if (user == null && !eventName.equals("init")) {
                event.reply(REDCROSS + " You don't own an alpaca, use **/init** first")
                     .setEphemeral(true)
                     .queue();
                return;
            }

            final IUserCommand userCmd = (IUserCommand) getCommand(eventName);
            userCmd.execute(event, user);
        } else {
            final IInfoCommand infoCmd = (IInfoCommand) getCommand(eventName);
            infoCmd.execute(event);
        }
    }

    public Map<String, ISlashCommand> getCommands() {
        return commands;
    }

    public String getCommandsAsString() {
        StringBuilder sb = new StringBuilder();
        commands.keySet().forEach(cmd -> sb.append("`").append(cmd).append("` "));

        return sb.toString();
    }

    private ISlashCommand getCommand(String eventName) {
        return commands.get(eventName);
    }

    private boolean isUserRequired(String eventName) {
        return commandInfo.get(eventName).get("requireUser").getAsBoolean();
    }
}
