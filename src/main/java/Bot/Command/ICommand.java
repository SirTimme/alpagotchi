package Bot.Command;

import Bot.Utils.PermissionLevel;
import net.dv8tion.jda.api.Permission;

import java.util.EnumSet;
import java.util.List;

public interface ICommand {
    void execute(CommandContext ctx);

    String getHelp(String prefix);

    String getName();

    Enum<PermissionLevel> getPermissionLevel();

    default List<String> getAliases() {
        return List.of();
    }

    EnumSet<Permission> getRequiredPermissions();
}
