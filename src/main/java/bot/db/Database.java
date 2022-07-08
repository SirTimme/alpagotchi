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
    private final MongoCollection<Entry> entries;
    private final MongoCollection<GuildSettings> guilds;

    public Database() {
        final var pojoRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        final var codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoRegistry);

        try (final var client = MongoClients.create(Env.get("DB_URI"))) {
            final var db = client.getDatabase(Env.get("DB_NAME")).withCodecRegistry(codecRegistry);

            this.entries = db.getCollection("alpacas_manager", Entry.class);
            this.guilds = db.getCollection("guild_settings", GuildSettings.class);
        }
    }

    @Override
    public Entry getUser(final long memberID) {
        return this.entries.find(eq("_id", memberID)).first();
    }

    @Override
    public void updateUser(final Entry entry) {
        this.entries.replaceOne(eq("_id", entry.getMemberID()), entry);
    }

    @Override
    public void createUser(final long memberID) {
        this.entries.insertOne(new Entry(memberID));
    }

    @Override
    public void deleteUser(final long memberID) {
        this.entries.deleteOne(eq("_id", memberID));
    }

    @Override
    public GuildSettings getGuildSettings(final long guildID) {
        GuildSettings settings = this.guilds.find(eq("_id", guildID)).first();

        if (settings == null) {
            settings = new GuildSettings(guildID);
            this.guilds.insertOne(settings);
        }

        return settings;
    }

    @Override
    public void updateGuildSettings(final GuildSettings settings) {
        this.guilds.replaceOne(eq("_id", settings.getGuildID()), settings);
    }

    @Override
    public long getEntries() {
        return this.entries.countDocuments();
    }
}