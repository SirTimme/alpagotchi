package bot.commands;

import bot.commands.dev.*;
import bot.commands.dev.Count;
import bot.commands.member.*;
import bot.db.IDatabase;
import bot.models.Entry;
import bot.models.GuildSettings;
import bot.shop.ItemManager;
import bot.utils.CommandType;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.text.MessageFormat;
import java.util.*;

public class CommandManager {
    private final Map<String, SlashCommand> commands = new TreeMap<>();

    public CommandManager() {
        final ItemManager items = new ItemManager();

        this.commands.put("ping", new Ping());
        this.commands.put("init", new Init());
        this.commands.put("currentBalance", new Balance());
        this.commands.put("buySuccessful", new Buy(items));
        this.commands.put("count", new Count());
        this.commands.put("deletion", new Delete());
        this.commands.put("feed", new Feed(items));
        this.commands.put("gift", new Gift());
        this.commands.put("image", new Image());
        this.commands.put("work", new Work());
        this.commands.put("shutdown", new Shutdown());
        this.commands.put("myalpaca", new MyAlpaca());
        this.commands.put("nick", new Nick());
        this.commands.put("help", new Help(this));
        this.commands.put("alpacaSleeping", new Sleep());
        this.commands.put("outfit", new Outfit());
        this.commands.put("pet", new Pet());
        this.commands.put("inventory", new Inventory(items));
        this.commands.put("shop", new Shop(items));
        this.commands.put("update", new Update(this));
        this.commands.put("language", new Language());
    }

    public void handle(final SlashCommandEvent event) {
        final GuildSettings settings = IDatabase.INSTANCE.getGuildSettings(event.getGuild().getIdLong());
        final Locale locale = settings.getLocale();

        final SlashCommand cmd = getCommand(event.getName());
        final Entry user = IDatabase.INSTANCE.getUser(event.getUser().getIdLong());

        if (cmd instanceof UserCommand && user == null) {
            MessageService.queueReply(event, new MessageFormat(Responses.get("alpacaNotOwned", locale)), true);
            return;
        }

        cmd.execute(event, locale, user);
    }

    public Collection<? extends SlashCommand> getCommands() {
        return this.commands.values();
    }

    public List<? extends CommandData> getCommandDataByTypes(final CommandType... types) {
        return this.commands.values()
                            .stream()
                            .filter(cmd -> Arrays.asList(types).contains(cmd.getCommandType()))
                            .map(SlashCommand::getCommandData)
                            .toList();
    }

    public String getCommandsAsString() {
        StringBuilder sb = new StringBuilder();

        this.commands.keySet()
                     .stream()
                     .filter(cmd -> getCommand(cmd).getCommandType() != CommandType.DEV)
                     .forEach(cmd -> sb.append("`").append(cmd).append("` "));

        return sb.toString();
    }

    private SlashCommand getCommand(final String name) {
        return this.commands.get(name);
    }
}
