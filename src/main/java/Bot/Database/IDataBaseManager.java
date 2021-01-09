package Bot.Database;

public interface IDataBaseManager {
    IDataBaseManager INSTANCE = new MongoDBDataSource();

    String getPrefix(long guildID);

    void setPrefix(long guildID, String newPrefix);

    String getNickname(long memberID);

    void setNickname(long memberID, String nickname);

    Integer getAlpacaValues(long memberID, String column);

    void setAlpacaValues(long memberID, String column, int newValue);

    Integer getInventory(long memberID, String column);

    void setInventory(long memberID, String column, int newValue);

    Long getCooldown(long memberID, String column);

    void setCooldown(long memberID, String column, long newValue);

    void decreaseValues();

    void createDBEntry(long memberID);

    boolean isUserInDB(long memberID);
}
