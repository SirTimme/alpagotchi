package bot.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class Resources {
	private static final ResourceBundle bundle = ResourceBundle.getBundle("messages_en_us", new Locale("en-us"));

	public static String getPattern(final String key) {
		return bundle.getString(key);
	}
}
