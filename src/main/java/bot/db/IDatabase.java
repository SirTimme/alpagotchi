package bot.db;

import bot.models.Alpaca;
import bot.models.Entry;
import bot.models.GuildSettings;

public interface IDatabase {
    IDatabase INSTANCE = new PostgresDB();

    Alpaca getUserById(final long memberID);

    void updateUser(final Entry entry);

    void createUserById(final long memberID);

    void deleteUserById(final long memberID);

    GuildSettings getSettingsById(final long guildID);

    void updateSettings(final GuildSettings settings);

    long getUserCount();
}