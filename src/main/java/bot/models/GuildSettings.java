package bot.models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Locale;

public class GuildSettings {
    @BsonProperty(value = "_id")
    private final long guildID;
    private String language;

    public GuildSettings(final long guildID) {
        this.guildID = guildID;
        this.language = "en";
    }

    @BsonCreator
    public GuildSettings(@BsonProperty(value = "_id") final long guildID,
            @BsonProperty(value = "language") final String language
    ) {
        this.guildID = guildID;
        this.language = language;
    }

    public long getGuildID() {
        return this.guildID;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    @BsonIgnore
    public Locale getLocale() {
        return Locale.forLanguageTag(this.language);
    }

    @BsonIgnore
    public void setLocale(final Locale locale) {
        this.language = locale.toLanguageTag();
    }
}
