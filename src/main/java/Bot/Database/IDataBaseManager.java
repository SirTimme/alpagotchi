package Bot.Database;

public interface IDataBaseManager {
    IDataBaseManager INSTANCE = new SQLiteDataSource();

    String getPrefix(long guildID);

    void setPrefix(long guildID, String newPrefix);

    String getAlpaca(long memberID, String column);

    void setAlpaca(long memberID, String column, String newValue);
}
