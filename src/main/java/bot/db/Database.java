package bot.db;

import bot.models.Entry;
import bot.models.GuildSettings;
import bot.utils.Env;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.model.Filters;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public class Database implements IDatabase {
    private final CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
    );

    private final MongoClientSettings clientSettings = MongoClientSettings.builder()
                                                                          .applyConnectionString(new ConnectionString(Env.get("DB_URI")))
                                                                          .codecRegistry(codecRegistry)
                                                                          .build();

    @Override
    public Entry getUserById(final long memberID) {
        try (final var client = MongoClients.create(clientSettings)) {
            final var db = client.getDatabase(Env.get("DB_NAME"));
            final var collection = db.getCollection("alpacas_manager", Entry.class);

            return collection.find(Filters.eq(memberID)).first();
        }
    }

    @Override
    public void updateUser(final Entry entry) {
        try (final var client = MongoClients.create(clientSettings)) {
            final var db = client.getDatabase(Env.get("DB_NAME"));
            final var collection = db.getCollection("alpacas_manager", Entry.class);

            collection.replaceOne(Filters.eq(entry.getMemberID()), entry);
        }
    }

    @Override
    public void createUserById(final long memberID) {
        try (final var client = MongoClients.create(clientSettings)) {
            final var db = client.getDatabase(Env.get("DB_NAME"));
            final var collection = db.getCollection("alpacas_manager", Entry.class);

            collection.insertOne(new Entry(memberID));
        }
    }

    @Override
    public void deleteUserById(final long memberID) {
        try (final var client = MongoClients.create(clientSettings)) {
            final var db = client.getDatabase(Env.get("DB_NAME"));
            final var collection = db.getCollection("alpacas_manager", Entry.class);

            collection.deleteOne(Filters.eq(memberID));
        }
    }

    @Override
    public GuildSettings getSettingsById(final long guildID) {
        try (final var client = MongoClients.create(clientSettings)) {
            final var db = client.getDatabase(Env.get("DB_NAME"));
            final var collection = db.getCollection("guild_settings", GuildSettings.class);

            var settings = collection.find(Filters.eq(guildID)).first();

            if (settings == null) {
                settings = new GuildSettings(guildID);
                collection.insertOne(settings);
            }

            return settings;
        }
    }

    @Override
    public void updateSettings(final GuildSettings settings) {
        try (final var client = MongoClients.create(clientSettings)) {
            final var db = client.getDatabase(Env.get("DB_NAME"));
            final var collection = db.getCollection("guild_settings", GuildSettings.class);

            collection.replaceOne(Filters.eq(settings.getGuildID()), settings);
        }
    }

    @Override
    public long getUserCount() {
        try (final var client = MongoClients.create(clientSettings)) {
            final var db = client.getDatabase(Env.get("DB_NAME"));
            final var collection = db.getCollection("alpacas_manager", Entry.class);

            return collection.countDocuments();
        }
    }
}