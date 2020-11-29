package Bot.Database;

public interface IDataBaseManager {
    IDataBaseManager INSTANCE = new SQLiteDataSource();

    String getPrefix(long guildID);

    void setPrefix(long guildID, String newPrefix);

    String getHunger(long memberID);

    String getThirst(long memberID);

    String getEnergy(long memberID);

    String getCurrency(long memberID);
}
