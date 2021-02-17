package Bot;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.Permission;

import java.util.EnumSet;

public class Config {
	private static final Dotenv dotenv = Dotenv.load();

	public static String get(String key) {
		return dotenv.get(key);
	}

	public static EnumSet<Permission> requiredPermissions() {
		return EnumSet.of(
				Permission.MESSAGE_WRITE,
				Permission.MESSAGE_ATTACH_FILES,
				Permission.VIEW_CHANNEL,
				Permission.MESSAGE_HISTORY,
				Permission.MESSAGE_EMBED_LINKS,
				Permission.MESSAGE_EXT_EMOJI,
				Permission.MESSAGE_READ,
				Permission.MESSAGE_MANAGE,
				Permission.MESSAGE_ADD_REACTION
		);
	}
}
