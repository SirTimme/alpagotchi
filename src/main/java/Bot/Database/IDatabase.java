package Bot.Database;

import Bot.Models.Entry;
import Bot.Utils.Stat;

public interface IDatabase {
	IDatabase INSTANCE = new MongoDB();

	Entry getEntry(long memberID);
	<T> void setEntry(long memberID, Stat stat, T newValue);

	String getPrefix(long guildID);

	void createEntry(long memberID);
	void deleteEntry(long memberID);

	void createGuild(long guildID);
	void deleteGuild(long guildID);

	void decreaseValues();
	long getEntries();
}
