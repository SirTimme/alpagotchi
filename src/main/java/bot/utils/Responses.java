package bot.utils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Manages multi language support for Alpagotchi
 */
public class Responses {
	/**
	 * @param key The key for the corresponding response in the resourcebundle
	 * @param locale The language in which the bot should respond
	 * @return The bots' response to the user
	 */
	public static String get(final String key, final Locale locale) {
		return ResourceBundle.getBundle("messages", locale).getString(key);
	}
}
