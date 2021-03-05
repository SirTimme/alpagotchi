package Bot.Utils;

import net.dv8tion.jda.api.Permission;

import java.util.HashMap;
import java.util.Map;

public class PermissionManager {
	private final static Map<String, Permission> permissions = new HashMap<>();

	public static void setPermissions() {
		permissions.put("MESSAGE_ADD_REACTION", Permission.MESSAGE_ADD_REACTION);
		permissions.put("MESSAGE_EMBED_LINKS", Permission.MESSAGE_EMBED_LINKS);
		permissions.put("MESSAGE_MANAGE", Permission.MESSAGE_MANAGE);
		permissions.put("MESSAGE_HISTORY", Permission.MESSAGE_HISTORY);
	}

	public static Permission getPermission(String permission) {
		return permissions.get(permission);
	}
}
