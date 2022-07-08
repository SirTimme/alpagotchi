package bot.db;

import bot.models.Entry;
import bot.models.GuildSettings;
import bot.utils.Env;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static com.mongodb.client.model.Filters.*;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Database implements IDatabase {
    private static final CodecRegistry pojoRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
    private static final CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoRegistry);

    @Override
    public Entry getUser(final long memberID) {
        try (final var client = MongoClients.create(Env.get("DB_URI"))) {
            final var db = client.getDatabase(Env.get("DB_NAME")).withCodecRegistry(codecRegistry);
            final var entries = db.getCollection("alpacas_manager", Entry.class);

            return entries.find(eq("_id", memberID)).first();
        }
    }

    @Override
    public void updateUser(final Entry entry) {
        try (final var client = MongoClients.create(Env.get("DB_URI"))) {
            final var db = client.getDatabase(Env.get("DB_NAME")).withCodecRegistry(codecRegistry);
            final var entries = db.getCollection("alpacas_manager", Entry.class);

            entries.replaceOne(eq("_id", entry.getMemberID()), entry);
        }
    }

    @Override
    public void createUser(final long memberID) {
        try (final var client = MongoClients.create(Env.get("DB_URI"))) {
            final var db = client.getDatabase(Env.get("DB_NAME")).withCodecRegistry(codecRegistry);
            final var entries = db.getCollection("alpacas_manager", Entry.class);

            entries.insertOne(new Entry(memberID));
        }
    }

    @Override
    public void deleteUser(final long memberID) {
        try (final var client = MongoClients.create(Env.get("DB_URI"))) {
            final var db = client.getDatabase(Env.get("DB_NAME")).withCodecRegistry(codecRegistry);
            final var entries = db.getCollection("alpacas_manager", Entry.class);

            entries.deleteOne(eq("_id", memberID));
        }
    }

    @Override
    public GuildSettings getGuildSettings(final long guildID) {
        try (final var client = MongoClients.create(Env.get("DB_URI"))) {
            final var db = client.getDatabase(Env.get("DB_NAME")).withCodecRegistry(codecRegistry);
            final var entries = db.getCollection("guild_settings", GuildSettings.class);

            GuildSettings settings = entries.find(eq("_id", guildID)).first();

            if (settings == null) {
                settings = new GuildSettings(guildID);
                entries.insertOne(settings);
            }

            return settings;
        }
    }

    @Override
    public void updateGuildSettings(final GuildSettings settings) {
        try (final var client = MongoClients.create(Env.get("DB_URI"))) {
            final var db = client.getDatabase(Env.get("DB_NAME")).withCodecRegistry(codecRegistry);
            final var entries = db.getCollection("guild_settings", GuildSettings.class);

            entries.replaceOne(eq("_id", settings.getGuildID()), settings);
        }
    }

    @Override
    public long getEntries() {
        try (final var client = MongoClients.create(Env.get("DB_URI"))) {
            final var db = client.getDatabase(Env.get("DB_NAME")).withCodecRegistry(codecRegistry);
            final var entries = db.getCollection("alpacas_manager", GuildSettings.class);

            return entries.countDocuments();
        }
    }
}