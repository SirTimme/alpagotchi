package Bot;

import Bot.Command.AdminCommands.SetPrefixCommand;
import Bot.Command.CommandContext;
import Bot.Command.MemberCommands.HelpCommand;
import Bot.Command.MemberCommands.MyAlpacaCommand;
import Bot.Command.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager() {
        addCommand(new MyAlpacaCommand());
        addCommand(new HelpCommand(this));
        addCommand(new SetPrefixCommand());
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
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if (cmd != null) {
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext commandContext = new CommandContext(event, args);
            cmd.handle(commandContext);
        }
    }
}
