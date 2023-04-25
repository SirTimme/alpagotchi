package bot.db;

import bot.models.Entry;
import bot.models.GuildSettings;
import bot.models.User;

public interface IDatabase {
    IDatabase INSTANCE = new PostgresDB();

    User getUserById(final long userId);

    void updateUser(final Entry entry);

    void createUserById(final long memberID);

    void deleteUserById(final long memberID);

    GuildSettings getSettingsById(final long guildID);

    void updateSettings(final GuildSettings settings);

    long getUserCount();
}