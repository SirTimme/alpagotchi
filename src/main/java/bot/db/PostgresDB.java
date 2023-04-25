package bot.db;

import bot.models.*;
import bot.utils.Env;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class PostgresDB implements IDatabase {
    private final SessionFactory sessionFactory;

    public PostgresDB() {
        final var registry = new StandardServiceRegistryBuilder()
                .configure()
                .applySetting("hibernate.connection.url", Env.get("POSTGRES_URL"))
                .build();

        this.sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    @Override
    public User getUserById(long userId) {
        var session = this.sessionFactory.openSession();
        var transaction = session.beginTransaction();

        var user = session
                .createQuery("FROM users u where u.user_id = :user_id", User.class)
                .setParameter("user_id", userId)
                .getSingleResult();

        transaction.commit();
        session.close();

        return user;
    }

    @Override
    public void updateUser(Entry entry) {

    }

    @Override
    public void createUserById(long userId) {
        var alpaca = new Alpaca();
        var inventory = new Inventory();
        var cooldown = new Cooldown();
        var user = new User(userId, alpaca, inventory, cooldown);

        var session = this.sessionFactory.openSession();
        var transaction = session.beginTransaction();

        session.persist(user);
        transaction.commit();
        session.close();
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