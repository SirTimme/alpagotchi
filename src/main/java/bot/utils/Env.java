package bot.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class Env {
	private static final Dotenv DOTENV = Dotenv.load(); // Loads content from .env file

	public static String get(final String key) {
		return DOTENV.get(key);
	}
}
