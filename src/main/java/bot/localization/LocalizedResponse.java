package bot.localization;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizedResponse {
    public static String get(final String key, final Locale locale, final Object... values) {
        final var format = new MessageFormat(ResourceBundle.getBundle("localization/responses", locale).getString(key));
        return format.format(values);
    }
}