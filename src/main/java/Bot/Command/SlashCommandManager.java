package Bot.Command;

import Bot.Command.Dev.Decrease;
import Bot.Command.Dev.Shutdown;
import Bot.Command.Member.*;
import Bot.Shop.ItemManager;
import com.mongodb.lang.Nullable;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.*;

public class SlashCommandManager {
    private Map<String, ISlashCommand> commands = new HashMap<>();

    public SlashCommandManager() {
        ItemManager itemMan = new ItemManager();

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
        commands.put("update", new Update());
    }

    public void handle(SlashCommandEvent event) {
        final ISlashCommand cmd = getCommand(event.getName());

        if (cmd != null) {
            cmd.execute(event, event.getUser().getIdLong());
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

    @Nullable
    public ISlashCommand getCommand(String cmd) {
        return this.commands.get(cmd);
    }
}
