package Bot.Command;

import Bot.Utils.Level;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;

public interface ICommand extends Comparable<ICommand> {
    void execute(CommandContext ctx);

    String getName();

    Level getLevel();

    default List<String> getAliases() {
        return List.of();
    }

    EnumSet<Permission> getCommandPerms();

    String getSyntax();

    default String getExample() { return getSyntax();};

    String getDescription();

    @Override
    default int compareTo(@NotNull ICommand cmd) {
        return this.getName().compareTo(cmd.getName());
    }
}
