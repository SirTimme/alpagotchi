package Bot.Database;

import Bot.Models.DBUser;

public interface IDatabase {
	IDatabase INSTANCE = new MongoDB();

	DBUser getUser(long memberID);
	void setUser(long memberID, DBUser DBUser);

	void createUser(long memberID);
	void deleteUser(long memberID);

	void createGuild(long guildID);
	void deleteGuild(long guildID);

	void decreaseValues();
	long getEntries();
}
