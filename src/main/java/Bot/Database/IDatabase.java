package Bot.Database;

public interface IDatabase {
	IDatabase INSTANCE = new MongoDB();

	String getPrefix(long guildID);

	void setPrefix(long guildID, String newPrefix);

	String getNickname(long memberID);

	void setNickname(long memberID, String newNickname);

	String getOutfit(long memberID);

	void setOutfit(long memberID, String newOutfit);

	int getAlpacaValues(long memberID, String column);

	void setAlpacaValues(long memberID, String column, int newValue);

	int getBalance(long memberID);

	void setBalance(long memberID, int newBalance);

	int getInventory(long memberID, String category, String item);

	void setInventory(long memberID, String category, String item, int newAmount);

	long getCooldown(long memberID, String column);

	void setCooldown(long memberID, String column, long newValue);

	void createUserEntry(long memberID);

	void deleteUserEntry(long memberID);

	void createGuildEntry(long guildID);

	void deleteGuildEntry(long guildID);

	boolean isUserInDB(long memberID);

	void decreaseValues();
}
