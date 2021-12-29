package bot.db;

import bot.models.Entry;
import bot.models.GuildSettings;

import java.util.Locale;

public interface IDatabase {
	IDatabase INSTANCE = new MongoDB();

	Entry getUser(final long memberID);

	void updateUser(final Entry DBUser);

	void createUser(final long memberID);

	void deleteUser(final long memberID);

	GuildSettings getGuildSettings(final long guildID);

	void setGuildSettings(final GuildSettings settings);

	long getEntries();
}
