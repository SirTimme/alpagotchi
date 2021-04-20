package Bot.Database;

import Bot.Shop.Item;
import Bot.Utils.Stat;
import org.bson.Document;

public interface IDatabase {
	IDatabase INSTANCE = new MongoDB();

	String getPrefix(long guildID);
	void setPrefix(long guildID, String newPrefix);

	int getStatInt(long memberID, Stat stat);
	void setStatInt(long memberID, Stat stat, int newValue);

	String getStatString(long memberID, Stat stat);
	void setStatString(long memberID, Stat stat, String newValue);

	long getStatLong(long memberID, Stat stat);
	void setStatLong(long memberID, Stat stat, long newValue);

	int getInventory(long memberID, Item item);
	void setInventory(long memberID, Item item, int newAmount);

	void createUser(long memberID);
	void deleteUser(long memberID);

	void createGuild(long guildID);
	void deleteGuild(long guildID);

	void decreaseValues();
	long getEntries();

	Document getUser(long memberID);
	Document getGuild(long guildID);
}
