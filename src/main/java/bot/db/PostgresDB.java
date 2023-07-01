package bot.db;

import bot.models.*;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.Session;

public class PostgresDB implements IDatabase {
    private EntityManagerFactory entityManagerFactory;

    @Override
    public void init() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("discord-bot");
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