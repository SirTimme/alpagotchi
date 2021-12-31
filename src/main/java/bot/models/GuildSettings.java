package bot.models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Locale;

/**
 * Represents per-guild settings
 */
public class GuildSettings {
	@BsonProperty(value = "_id")
	private final long guildID; // The guild id in discord
	private String language; // The guilds language

	/**
	 * Constructor used for creating a new db entry
	 * @param guildID The guild id in discord
	 */
	public GuildSettings(final long guildID) {
		this.guildID = guildID;
		this.language = "en";
	}

	/**
	 * Constructor used for serialization from the db
	 * @param guildID The guild id in discord
	 * @param language The chosen language
	 */
	@BsonCreator
	public GuildSettings(@BsonProperty(value = "_id") final long guildID,
						 @BsonProperty(value = "language") final String language
	) {
		this.guildID = guildID;
		this.language = language;
	}

	/**
	 * Returns the id of the guild
	 * @return The guild id
	 */
	public long getGuildID() {
		return this.guildID;
	}

	/**
	 * Returns the language as the two character language tag
	 * @return The guilds language
	 */
	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(final String language) {
		this.language = language;
	}

	/**
	 * Retrieves the language of a guild as a Locale object
	 * @return The locale
	 */
	@BsonIgnore
	public Locale getLocale() {
		return Locale.forLanguageTag(this.language);
	}

	/**
	 * Sets the locale of a guild
	 * @param locale The new locale
	 */
	@BsonIgnore
	public void setLocale(final Locale locale) {
		this.language = locale.toLanguageTag();
	}
}
