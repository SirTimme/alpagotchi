package Bot.Database;

import java.util.List;

public interface IDataBaseManager {
    IDataBaseManager INSTANCE = new SQLiteDataSource();

    String getPrefix(long guildID);

    int getAlpaca(long memberID, String column);

    int getInventory(long memberID, String column);

    long getCooldown(long memberID, String column);

    void decreaseValues();

    void setPrefix(long guildID, String newPrefix);

    void setAlpaca(long memberID, String column, int newValue);

    void setInventory(long memberID, String column, int newValue);

    void setCooldown(long memberID, String column, long newValue);
}
