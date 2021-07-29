package Bot.Database;

import Bot.Models.User;

public interface IDatabase {
	IDatabase INSTANCE = new MongoDB();

	User getUser(long memberID);
	void setUser(long memberID, User user);

	String getPrefix(long guildID);

	void createUser(long memberID);
	void deleteUser(long memberID);

	void createGuild(long guildID);
	void deleteGuild(long guildID);

	void decreaseValues();
	long getEntries();
}
