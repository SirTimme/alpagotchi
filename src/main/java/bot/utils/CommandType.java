package bot.utils;

/**
 * Determines the category of a slashcommand
 */
public enum CommandType {
	INFO, // Does not require a user
	DEV, // Dev-only
	USER, // Requires a user
}
