package bot.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class Responses {
    public static String get(final String key, final Locale locale) {
        return ResourceBundle.getBundle("languages/responses", locale).getString(key);
    }
}
