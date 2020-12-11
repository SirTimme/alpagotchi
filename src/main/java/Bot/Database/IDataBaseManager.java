package Bot.Database;

public interface IDataBaseManager {
    IDataBaseManager INSTANCE = new SQLiteDataSource();

    String getPrefix(long guildID);

    void setPrefix(long guildID, String newPrefix);

    String getNickname(long memberID);

    void setNickname(long memberID, String nickname);

    int getStatus(long memberID, String column);

    void setStatus(long memberID, String column, int newValue);

    int getInventory(long memberID, String column);

    void setInventory(long memberID, String column, int newValue);

    long getCooldown(long memberID, String column);

    void setCooldown(long memberID, String column, long newValue);

    void decreaseValues();
}
