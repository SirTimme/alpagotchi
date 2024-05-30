package dev.sirtimme.alpagotchi.commands;

import dev.sirtimme.alpagotchi.commands.member.Language;
import dev.sirtimme.alpagotchi.commands.owner.Count;
import dev.sirtimme.alpagotchi.commands.owner.Set;
import dev.sirtimme.alpagotchi.commands.owner.Shutdown;
import dev.sirtimme.alpagotchi.commands.owner.Update;
import dev.sirtimme.alpagotchi.commands.member.*;
import dev.sirtimme.alpagotchi.shop.ItemManager;
import dev.sirtimme.alpagotchi.commands.types.CommandType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CommandManager {
    private final Map<String, ISlashCommand> commands;

    public CommandManager() {
        final var itemManager = new ItemManager();

        this.commands = new TreeMap<>();
        this.commands.put("ping", new Ping());
        this.commands.put("init", new Init());
        this.commands.put("balance", new Balance());
        this.commands.put("buy", new Buy(itemManager));
        this.commands.put("count", new Count());
        this.commands.put("delete", new Delete());
        this.commands.put("feed", new Feed(itemManager));
        this.commands.put("gift", new Gift());
        this.commands.put("work", new Work());
        this.commands.put("shutdown", new Shutdown());
        this.commands.put("alpaca", new Alpaca());
        this.commands.put("nick", new Nick());
        this.commands.put("help", new Help(this));
        this.commands.put("sleep", new Sleep());
        this.commands.put("outfit", new Outfit());
        this.commands.put("pet", new Pet());
        this.commands.put("inventory", new Inventory(itemManager));
        this.commands.put("shop", new Shop(itemManager));
        this.commands.put("update", new Update(this));
        this.commands.put("language", new Language());
        this.commands.put("set", new Set());
    }

    public void handle(final SlashCommandInteractionEvent event) {
        final var cmd = this.commands.get(event.getName());

        cmd.execute(event);
    }

    public Collection<ISlashCommand> getCommands() {
        return this.commands.values();
    }

    public List<CommandData> getCommandDataByTypes(final CommandType... types) {
        return this.commands.values()
                            .stream()
                            .filter(cmd -> List.of(types).contains(cmd.getCommandType()))
                            .map(ISlashCommand::getCommandData)
                            .toList();
    }

    public String getCommandNames() {
        final var builder = new StringBuilder();

        this.commands.entrySet()
                     .stream()
                     .filter(entry -> entry.getValue().getCommandType() != CommandType.OWNER)
                     .forEach(entry -> builder.append(entry.getKey()).append("\n"));

        return builder.toString();
    }
}