package Bot.Database;

public interface IDataBaseManager {
    IDataBaseManager INSTANCE = new MongoDBDataSource();

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

    void decreaseValues();

    void createDBEntry(long memberID);

    boolean isUserInDB(long memberID);
}
