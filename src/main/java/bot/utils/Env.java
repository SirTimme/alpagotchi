package bot.utils;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Loads environment variables on startup to use them during runtime
 */
public class Env {
	/**
	 * Loads content from .env file
	 */
	private static final Dotenv dotenv = Dotenv.load();

	/**
	 * Retrieves env var by key
	 * @param key The key to the corresponding env var
	 * @return The content of the env var
	 */
	public static String get(String key) {
		return dotenv.get(key);
	}
}
