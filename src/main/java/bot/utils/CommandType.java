package bot.utils;

/**
 * Determines the category of a slashcommand
 */
public enum CommandType {
	INFO, // Does not require a user
	DEV, // Dev-only
	STATIC_USER, // Requires a user but does not modify its stats
	DYNAMIC_USER // Requires a user and modify its stats
}
