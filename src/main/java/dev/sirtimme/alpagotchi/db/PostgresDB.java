package dev.sirtimme.alpagotchi.db;

import dev.sirtimme.alpagotchi.models.alpaca.Alpaca;
import dev.sirtimme.alpagotchi.models.cooldown.Cooldown;
import dev.sirtimme.alpagotchi.models.guildsettings.GuildSettings;
import dev.sirtimme.alpagotchi.models.inventory.Inventory;
import dev.sirtimme.alpagotchi.models.user.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.Session;

import java.util.HashMap;

public class PostgresDB implements IDatabase {
    private EntityManagerFactory entityManagerFactory;

    @Override
    public void initDatabase() {
        final var properties = new HashMap<String, String>() {{
            put("jakarta.persistence.jdbc.user", System.getenv("POSTGRES_USER"));
            put("jakarta.persistence.jdbc.password", System.getenv("POSTGRES_PASSWORD"));
            put("jakarta.persistence.jdbc.url", System.getenv("POSTGRES_URL"));
        }};

        this.entityManagerFactory = Persistence.createEntityManagerFactory("discord-bot", properties);
    }

    @Override
    public void shutdownDatabase() {
        this.entityManagerFactory.close();
    }

    @Override
    public User getUserById(final long userId) {
        final var entityManager = this.entityManagerFactory.createEntityManager();

        final var user = entityManager
                .unwrap(Session.class)
                .bySimpleNaturalId(User.class)
                .load(userId);

        entityManager.close();

        return user;
    }

    @Override
    public void createUserById(final long userId) {
        final var alpaca = new Alpaca();
        final var inventory = new Inventory();
        final var cooldown = new Cooldown();
        final var user = new User(userId, alpaca, inventory, cooldown);

        final var entityManager = this.entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();

        entityManager.close();
    }

    @Override
    public void updateUser(final User user) {
        final var entityManager = this.entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.merge(user);
        entityManager.getTransaction().commit();

        entityManager.close();
    }

    @Override
    public void deleteUserById(final long userId) {
        final var entityManager = this.entityManagerFactory.createEntityManager();

        final var user = entityManager
                .unwrap(Session.class)
                .bySimpleNaturalId(User.class)
                .load(userId);

        entityManager.getTransaction().begin();
        entityManager.remove(user);
        entityManager.getTransaction().commit();

        entityManager.close();
    }

    @Override
    public GuildSettings getSettingsById(final long guildId) {
        final var entityManager = this.entityManagerFactory.createEntityManager();

        final var settings = entityManager
                .unwrap(Session.class)
                .bySimpleNaturalId(GuildSettings.class)
                .load(guildId);

        entityManager.close();

        return settings;
    }

    @Override
    public void updateSettings(final GuildSettings settings) {
        final var entityManager = this.entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.merge(settings);
        entityManager.getTransaction().commit();

        entityManager.close();
    }

    @Override
    public long getUserCount() {
        final var entityManager = this.entityManagerFactory.createEntityManager();
        final var query = entityManager.createQuery("SELECT COUNT(*) FROM User");

        return (long) query.getSingleResult();
    }
}