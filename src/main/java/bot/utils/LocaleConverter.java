package bot.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Locale;

@Converter
public class LocaleConverter implements AttributeConverter<Locale, String> {
    @Override
    public String convertToDatabaseColumn(Locale locale) {
        return locale.toLanguageTag();
    }

    @Override
    public Locale convertToEntityAttribute(String languageTag) {
        return Locale.forLanguageTag(languageTag);
    }
}