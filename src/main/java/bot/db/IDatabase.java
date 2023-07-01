package bot.db;

import bot.models.GuildSettings;
import bot.models.User;

public interface IDatabase {
    IDatabase INSTANCE = new PostgresDB();

    void init();

    void shutdown();

    User getUserById(final long userId);

    void updateDatabase(final User user);

    void createUserById(final long memberID);

    void deleteUserById(final long memberID);

    GuildSettings getSettingsById(final long guildID);

    void updateSettings(final GuildSettings settings);

    long getUserCount();
}