package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.CommandManager;
import Bot.Command.ICommand;
import Bot.Config;
import Bot.Constants;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class HelpCommand implements ICommand {
    private final CommandManager cmdManager;

    public HelpCommand(CommandManager manager) {
        this.cmdManager = manager;
    }

    @Override
    public void handle(CommandContext commandContext) {
        List<String> args = commandContext.getArgs();
        TextChannel channel = commandContext.getChannel();

        if (args.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            String prefix = Constants.PREFIXES.get(commandContext.getGuild().getIdLong());

            builder.append("List of Commands\n");

            cmdManager.getCommands().stream().map(ICommand::getName).forEach(
                    cmd -> builder.append("`").append(prefix).append(cmd).append("`\n")
            );

            channel.sendMessage(builder.toString()).queue();
            return;
        }

        String search = args.get(0);
        ICommand command = cmdManager.getCommand(search);

        if (command == null) {
            channel.sendMessage("<:RedCross:782229279312314368> No help found for " + search).queue();
            return;
        }

        channel.sendMessage(command.getHelp()).queue();
    }

    @Override
    public String getHelp() {
        return "`Usage: a!help [command]`\nDisplays further information to a specific command";
    }

    @Override
    public String getName() {
        return "help";
    }
}
