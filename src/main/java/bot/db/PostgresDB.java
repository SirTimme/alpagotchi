package bot.db;

import bot.models.*;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.Session;

import java.util.HashMap;

public class PostgresDB implements IDatabase {
    private EntityManagerFactory entityManagerFactory;

    @Override
    public void init() {
        final var properties = new HashMap<String, String>() {{
            put("hibernate.hikari.dataSource.user", System.getenv("POSTGRES_USER"));
            put("hibernate.hikari.dataSource.password", System.getenv("POSTGRES_PASSWORD"));
            put("hibernate.hikari.dataSource.url", System.getenv("POSTGRES_URL"));
        }};

        this.entityManagerFactory = Persistence.createEntityManagerFactory("discord-bot", properties);
    }

    @Override
    public void shutdown() {
        this.entityManagerFactory.close();
    }

    @Override
    public User getUserById(long userId) {
        var entityManager = this.entityManagerFactory.createEntityManager();

        var user = entityManager
                .unwrap(Session.class)
                .bySimpleNaturalId(User.class)
                .load(userId);

        entityManager.close();

        return user;
    }

    @Override
    public void updateDatabase(User user) {
        var entityManager = this.entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.merge(user);
        entityManager.getTransaction().commit();

        entityManager.close();
    }

    @Override
    public void createUserById(long userId) {
        var alpaca = new Alpaca();
        var inventory = new Inventory();
        var cooldown = new Cooldown();
        var user = new User(userId, alpaca, inventory, cooldown);

        var entityManager = this.entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();

        entityManager.close();
    }

    @Override
    public void deleteUserById(long userId) {
        var entityManager = this.entityManagerFactory.createEntityManager();

        var user = entityManager
                .unwrap(Session.class)
                .bySimpleNaturalId(User.class)
                .load(userId);

        entityManager.getTransaction().begin();
        entityManager.remove(user);
        entityManager.getTransaction().commit();

        entityManager.close();
    }

    @Override
    public GuildSettings getSettingsById(long guildId) {
        var entityManager = this.entityManagerFactory.createEntityManager();

        var settings = entityManager
                .unwrap(Session.class)
                .bySimpleNaturalId(GuildSettings.class)
                .load(guildId);

        entityManager.close();

        return settings;
    }

    @Override
    public void updateSettings(GuildSettings settings) {
        var entityManager = this.entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.merge(settings);
        entityManager.getTransaction().commit();

        entityManager.close();
    }

    @Override
    public long getUserCount() {
        var entityManager = this.entityManagerFactory.createEntityManager();
        var query = entityManager.createQuery("SELECT COUNT(*) FROM User");

        return (long) query.getSingleResult();
    }
}