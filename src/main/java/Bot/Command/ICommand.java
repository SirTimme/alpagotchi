package Bot.Command;

public interface ICommand {

    void handle(CommandContext commandContext);

    String getHelp(CommandContext commandContext);

    String getName();
}
