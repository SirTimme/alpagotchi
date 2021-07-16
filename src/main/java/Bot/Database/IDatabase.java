package Bot.Database;

import Bot.Models.Entry;

public interface IDatabase {
	IDatabase INSTANCE = new MongoDB();

	Entry getEntry(long memberID);
	void setEntry(long memberID, Entry entry);

	String getPrefix(long guildID);

	void createEntry(long memberID);
	void deleteEntry(long memberID);

	void createGuild(long guildID);
	void deleteGuild(long guildID);

	void decreaseValues();
	long getEntries();
}
