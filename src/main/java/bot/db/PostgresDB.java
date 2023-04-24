package bot.db;

import bot.models.Alpaca;
import bot.models.Entry;
import bot.models.GuildSettings;
import bot.utils.Env;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class PostgresDB implements IDatabase {
    private SessionFactory sessionFactory;

    public PostgresDB() {
        final var registry = new StandardServiceRegistryBuilder()
                .configure()
                .applySetting("hibernate.connection.url", Env.get("POSTGRES_URL"))
                .build();

        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    @Override
    public Alpaca getUserById(long memberID) {
        return null;
    }

    @Override
    public void updateUser(Entry entry) {

    }

    @Override
    public void createUserById(long memberID) {

    }

    @Override
    public void deleteUserById(long memberID) {

    }

    @Override
    public GuildSettings getSettingsById(long guildID) {
        return null;
    }

    @Override
    public void updateSettings(GuildSettings settings) {

    }

    @Override
    public long getUserCount() {
        return 0;
    }
}