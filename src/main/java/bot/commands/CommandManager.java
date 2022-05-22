package bot.commands;

import bot.commands.dev.*;
import bot.commands.dev.Count;
import bot.commands.member.*;
import bot.db.IDatabase;
import bot.models.Entry;
import bot.shop.ItemManager;
import bot.utils.CommandType;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.text.MessageFormat;
import java.util.*;

public class CommandManager {
    private final Map<String, ISlashCommand> commands = new TreeMap<>();

    public CommandManager() {
        final ItemManager items = new ItemManager();

        this.commands.put("ping", new Ping());
        this.commands.put("init", new Init());
        this.commands.put("balance", new Balance());
        this.commands.put("buy", new Buy(items));
        this.commands.put("count", new Count());
        this.commands.put("delete", new Delete());
        this.commands.put("feed", new Feed(items));
        this.commands.put("gift", new Gift());
        this.commands.put("image", new Image());
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

    public void handle(final SlashCommandEvent event) {
        final Locale locale = getLocale(event);
        final ISlashCommand cmd = getCommand(event.getName());
        final Entry user = IDatabase.INSTANCE.getUser(event.getUser().getIdLong());

        if (cmd.getCommandType() == CommandType.USER && user == null) {
            final var format = new MessageFormat(Responses.get("alpacaNotOwned", locale));
            final var msg = format.format(new Object[]{});

            event.reply(msg).setEphemeral(true).queue();
            return;
        }

        cmd.execute(event, locale, user);
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

    public String getCommandsAsString() {
        final StringBuilder sb = new StringBuilder();

        this.commands.keySet()
                     .stream()
                     .filter(cmd -> getCommand(cmd).getCommandType() != CommandType.DEV)
                     .forEach(cmd -> sb.append("`").append(cmd).append("` "));

        return sb.toString();
    }

    private ISlashCommand getCommand(final String name) {
        return this.commands.get(name);
    }

    private Locale getLocale(final SlashCommandEvent event) {
        return event.getGuild() == null
                ? Locale.ENGLISH
                : IDatabase.INSTANCE.getGuildSettings(event.getGuild().getIdLong()).getLocale();
    }
}