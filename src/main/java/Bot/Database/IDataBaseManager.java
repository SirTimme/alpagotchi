package Bot.Database;

public interface IDataBaseManager {
    IDataBaseManager INSTANCE = new SQLiteDataSource();

    String getPrefix(long guildID);

    void setPrefix(long guildID, String newPrefix);

    String getAlpacaStats(long memberID, String keyWord);

    void setAlpacaStats(long memberID, String newValue, String keyWord, String operator);
}
