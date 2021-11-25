package bot.utils;

/**
 * Determines the category of a slashcommand
 */
public enum CommandType {
	/**
	 * Does not require a user
	 */
	INFO,
	/**
	 * Only the dev is allowed to execute
	 */
	DEV,
	/**
	 * Requires a user but does not modify its stats
	 */
	STATIC_USER,
	/**
	 * Requires a user and modify its stats
	 */
	DYNAMIC_USER
}
