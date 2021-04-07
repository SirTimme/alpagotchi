package Bot.Database;

import Bot.Shop.IShopItem;
import Bot.Utils.Activity;
import Bot.Utils.Stat;

public interface IDatabase {
	IDatabase INSTANCE = new MongoDB();

	String getPrefix(long guildID);

	void setPrefix(long guildID, String newPrefix);

	String getNickname(long memberID);

	void setNickname(long memberID, String newNickname);

	String getOutfit(long memberID);

	void setOutfit(long memberID, String newOutfit);

	int getStat(long memberID, Stat stat);

	void setStat(long memberID, Stat stat, int newValue);

	int getBalance(long memberID);

	void setBalance(long memberID, int newBalance);

	int getInventory(long memberID, IShopItem item);

	void setInventory(long memberID, IShopItem item, int newAmount);

	long getCooldown(long memberID, Activity activity);

	void setCooldown(long memberID, Activity activity, long newValue);

	void createUserEntry(long memberID);

	void deleteUserEntry(long memberID);

	void createGuildEntry(long guildID);

	void deleteGuildEntry(long guildID);

	boolean isUserInDB(long memberID);

	void decreaseValues();

	long getAll();
}
