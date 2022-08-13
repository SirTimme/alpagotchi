package bot.db;

import bot.models.Entry;
import bot.models.GuildSettings;
import bot.utils.Env;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.model.Filters;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;

public class Database implements IDatabase {
    private MongoClient client;

    @Override
    public void connect() {
        final var codecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );

        final var settings = MongoClientSettings.builder()
                                                .applyConnectionString(new ConnectionString(Env.get("DB_URI")))
                                                .codecRegistry(codecRegistry)
                                                .build();

        this.client = MongoClients.create(settings);
    }

    @Override
    public Entry getUserById(final long memberID) {
        final var db = this.client.getDatabase(Env.get("DB_NAME"));
        final var collection = db.getCollection("alpacas_manager", Entry.class);

        return collection.find(Filters.eq(memberID)).first();
    }

    @Override
    public void updateUser(final Entry entry) {
        final var db = this.client.getDatabase(Env.get("DB_NAME"));
        final var collection = db.getCollection("alpacas_manager", Entry.class);

        collection.replaceOne(Filters.eq(entry.getMemberID()), entry);
    }

    @Override
    public void createUserById(final long memberID) {
        final var db = this.client.getDatabase(Env.get("DB_NAME"));
        final var collection = db.getCollection("alpacas_manager", Entry.class);

        collection.insertOne(new Entry(memberID));
    }

    @Override
    public void deleteUserById(final long memberID) {
        final var db = this.client.getDatabase(Env.get("DB_NAME"));
        final var collection = db.getCollection("alpacas_manager", Entry.class);

        collection.deleteOne(Filters.eq(memberID));
    }

    @Override
    public GuildSettings getSettingsById(final long guildID) {
        final var db = this.client.getDatabase(Env.get("DB_NAME"));
        final var collection = db.getCollection("guild_settings", GuildSettings.class);

        var settings = collection.find(Filters.eq(guildID)).first();

        if (settings == null) {
            settings = new GuildSettings(guildID);
            collection.insertOne(settings);
        }

        return settings;
    }

    @Override
    public void updateSettings(final GuildSettings settings) {
        final var db = this.client.getDatabase(Env.get("DB_NAME"));
        final var collection = db.getCollection("guild_settings", GuildSettings.class);

        collection.replaceOne(Filters.eq(settings.getGuildID()), settings);
    }

    @Override
    public long getUserCount() {
        final var db = this.client.getDatabase(Env.get("DB_NAME"));
        final var collection = db.getCollection("alpacas_manager", Entry.class);

        return collection.countDocuments();
    }
}