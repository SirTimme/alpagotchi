package Bot.Database;

public interface IDataBaseManager {
    IDataBaseManager INSTANCE = new SQLiteDataSource();

    String getPrefix(long guildID);

    void setPrefix(long guildID, String newPrefix);

    int getAlpaca(long memberID, String column);

    void setAlpaca(long memberID, String column, int newValue);

    int getInventory(long memberID, String column);

    void setInventory(long memberID, String column, int newValue);

    long getCooldown(long memberID, String column);

    void setCooldown(long memberID, String column, long newValue);
}
