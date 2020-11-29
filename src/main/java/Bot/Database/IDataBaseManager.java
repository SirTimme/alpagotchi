package Bot.Database;

public interface IDataBaseManager {
    IDataBaseManager INSTANCE = new SQLiteDataSource();

    String getPrefix(long guildID);

    void setPrefix(long guildID, String newPrefix);

    String getAlpacaStats(long memberID, String keyWord);

    void setHunger(long memberID, String newValue);

    void setThirst(long memberID, String newValue);

    void setEnergy(long memberID, String newValue);

    void setCurrency(long memberID, String newValue);
}
