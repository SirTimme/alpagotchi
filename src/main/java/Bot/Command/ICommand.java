package Bot.Command;

import Bot.Utils.PermLevel;
import net.dv8tion.jda.api.Permission;

import java.util.EnumSet;
import java.util.List;

public interface ICommand {
    void execute(CommandContext ctx);

    String getName();

    PermLevel getPermLevel();

    default List<String> getAliases() {
        return List.of();
    }

    EnumSet<Permission> getCommandPerms();

    String getSyntax();

    default String getExample() { return getSyntax();};

    String getDescription();
}
