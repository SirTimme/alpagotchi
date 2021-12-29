package bot.utils;

/**
 * Determines the category of a slashcommand
 */
public enum CommandType {
	INIT, // Used for initialization
	INFO, // Does not require a user
	DEV, // Dev-only
	USER, // Requires a user
}
