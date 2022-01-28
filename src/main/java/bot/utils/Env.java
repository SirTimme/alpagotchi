package bot.utils;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Loads environment variables on startup to use them during runtime
 */
public class Env {
	private static final Dotenv DOTENV = Dotenv.load(); // Loads content from .env file

	/**
	 * Retrieves env var by key
	 * @param key The key to the corresponding env var
	 * @return The content of the env var
	 */
	public static String get(final String key) {
		return DOTENV.get(key);
	}
}
