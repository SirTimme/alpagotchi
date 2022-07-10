package bot.commands;

import bot.commands.dev.*;
import bot.commands.member.*;
import bot.shop.ItemManager;
import bot.utils.CommandType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.*;

public class CommandManager {
    private final Map<String, ISlashCommand> commands;

    public CommandManager() {
        final var items = new ItemManager();

        this.commands = new TreeMap<>();
        this.commands.put("ping", new Ping());
        this.commands.put("init", new Init());
        this.commands.put("balance", new Balance());
        this.commands.put("buy", new Buy(items));
        this.commands.put("count", new Count());
        this.commands.put("delete", new Delete());
        this.commands.put("feed", new Feed(items));
        this.commands.put("gift", new Gift());
        this.commands.put("work", new Work());
        this.commands.put("shutdown", new Shutdown());
        this.commands.put("myalpaca", new MyAlpaca());
        this.commands.put("nick", new Nick());
        this.commands.put("help", new Help(this));
        this.commands.put("sleep", new Sleep());
        this.commands.put("outfit", new Outfit());
        this.commands.put("pet", new Pet());
        this.commands.put("inventory", new Inventory(items));
        this.commands.put("shop", new Shop(items));
        this.commands.put("update", new Update(this));
        this.commands.put("language", new Language());
    }

    public void handle(final SlashCommandInteractionEvent event) {
        final var cmd = getCommandByName(event.getName());

        cmd.execute(event);
    }

    public Collection<? extends ISlashCommand> getCommands() {
        return this.commands.values();
    }

    public List<? extends CommandData> getCommandDataByTypes(final CommandType... types) {
        return this.commands.values()
                            .stream()
                            .filter(cmd -> Arrays.asList(types).contains(cmd.getCommandType()))
                            .map(ISlashCommand::getCommandData)
                            .toList();
    }

    public String getCommandNames() {
        final var sb = new StringBuilder();

        this.commands.keySet()
                     .stream()
                     .filter(name -> getCommandByName(name).getCommandType() != CommandType.DEV)
                     .forEach(name -> sb.append("`").append(name).append("`").append(" "));

        return sb.toString();
    }

    private ISlashCommand getCommandByName(final String name) {
        return this.commands.get(name);
    }
}