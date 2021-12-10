package bot.models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Locale;

/**
 * Represents per-guild settings
 */
public class GuildSettings {
	@BsonProperty(value = "_id")
	private final long guildID; // The guild id in discord
	private Locale locale; // The local language

	/**
	 * Constructor used for creating a new db entry
	 * @param guildID The guild id in discord
	 */
	public GuildSettings(final long guildID) {
		this.guildID = guildID;
		this.locale = Locale.ENGLISH;
	}

	/**
	 * Constructor used for serialization from the db
	 * @param guildID The guild id in discord
	 * @param locale The chosen language
	 */
	@BsonCreator
	public GuildSettings(@BsonProperty(value = "_id") final long guildID,
						 @BsonProperty(value = "locale") final Locale locale
	) {
		this.guildID = guildID;
		this.locale = locale;
	}

	/**
	 * Returns the guild id
	 * @return The guild id
	 */
	public long getGuildID() {
		return this.guildID;
	}

	/**
	 * Sets the locale of a guild
	 * @param locale The new locale
	 */
	public void setLocale(final Locale locale) {
		this.locale = locale;
	}

	/**
	 * Retrieves the locale of a guild
	 * @return The locale
	 */
	public Locale getLocale() {
		return this.locale;
	}
}
