package bot.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class Responses {
    public static String getLocalizedResponse(final String key, final Locale locale) {
        return ResourceBundle.getBundle("localization/responses", locale).getString(key);
    }
}