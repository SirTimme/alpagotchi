package bot.utils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Manages multi language support for Alpagotchi
 */
public class Responses {
	/**
	 * Holds all responses for the english version
	 */
	private static final ResourceBundle bundle = ResourceBundle.getBundle("languages/en_us", new Locale("en-us"));

	/**
	 * Retrieves a response by key
	 * @param key The key for the corresponding response in the resourcebundle
	 * @return The bots' response to the user
	 */
	public static String get(final String key) {
		return bundle.getString(key);
	}
}
