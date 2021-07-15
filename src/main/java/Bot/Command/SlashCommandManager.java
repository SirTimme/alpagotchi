package Bot.Command;

import Bot.Command.Dev.Decrease;
import Bot.Command.Dev.Shutdown;
import Bot.Command.Dev.Update;
import Bot.Command.Member.*;
import Bot.Shop.ItemManager;
import com.mongodb.lang.Nullable;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.*;

public class SlashCommandManager {
    private final Map<String, ISlashCommand> commands = new TreeMap<>();

    public SlashCommandManager() {
        ItemManager itemMan = new ItemManager();

        this.commands.put("ping", new Ping());
        this.commands.put("init", new Init());
        this.commands.put("balance", new Balance());
        this.commands.put("buy", new Buy(itemMan));
        this.commands.put("count", new Count());
        this.commands.put("delete", new Delete());
        this.commands.put("feed", new Feed(itemMan));
        this.commands.put("gift", new Gift(itemMan));
        this.commands.put("image", new Image());
        this.commands.put("work", new Work());
        this.commands.put("shutdown", new Shutdown());
        this.commands.put("decrease", new Decrease());
        this.commands.put("myalpaca", new MyAlpaca());
        this.commands.put("nick", new Nick());
        this.commands.put("help", new Help(this));
        this.commands.put("sleep", new Sleep());
        this.commands.put("outfit", new Outfit());
        this.commands.put("pet", new Pet());
        this.commands.put("inventory", new Inventory(itemMan));
        this.commands.put("shop", new Shop(itemMan));
        this.commands.put("update", new Update());
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
        this.commands.keySet().forEach(cmd -> sb.append("`/").append(cmd).append("`\n"));

        return sb.toString();
    }

    @Nullable
    public ISlashCommand getCommand(String cmd) {
        return this.commands.get(cmd);
    }
}
