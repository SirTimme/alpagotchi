package Bot.Handler;

import Bot.Command.DeveloperCommands.Decrease;
import Bot.Command.AdminCommands.SetPrefix;
import Bot.Command.CommandContext;
import Bot.Command.DeveloperCommands.Shutdown;
import Bot.Command.MemberCommands.*;
import Bot.Command.ICommand;
import Bot.Shop.ShopItemManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();
    ShopItemManager shopItemManager = new ShopItemManager();

    public CommandManager() {
        addCommand(new MyAlpaca());
        addCommand(new Help(this));
        addCommand(new SetPrefix());
        addCommand(new Wallet());
        addCommand(new Work());
        addCommand(new Shop(this.shopItemManager));
        addCommand(new Buy(this.shopItemManager));
        addCommand(new Inventory());
        addCommand(new Feed(this.shopItemManager));
        addCommand(new Decrease());
        addCommand(new Nick());
        addCommand(new Pet());
        addCommand(new Gift(this.shopItemManager));
        addCommand(new Shutdown());
    }

    private void addCommand(ICommand command) {
        boolean nameFound = this.commands.stream().anyMatch(cmd -> cmd.getName().equalsIgnoreCase(command.getName()));

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name already exists!");
        }
        commands.add(command);
    }

    public List<ICommand> getCommands() {
        return commands;
    }

    @Nullable
    public ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ICommand cmd : this.commands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }
        return null;
    }

    void handle(GuildMessageReceivedEvent event, String prefix) {
        String[] split = event.getMessage()
                .getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if (cmd == null) {
            return;
        }

        List<String> args = Arrays.asList(split).subList(1, split.length);

        CommandContext commandContext = new CommandContext(event, args);
        cmd.handle(commandContext);
    }
}
