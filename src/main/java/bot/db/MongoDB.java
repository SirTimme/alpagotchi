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

public class MongoDB implements IDatabase {
    private final MongoCollection<Entry> entries;
    private final MongoCollection<GuildSettings> guilds;

    public MongoDB() {
        final CodecRegistry pojoRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        final CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoRegistry);

        final MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(Env.get("DB_URI")))
                .codecRegistry(codecRegistry)
                .build();

        final MongoClient client = MongoClients.create(settings);
        final MongoDatabase db = client.getDatabase(Env.get("DB_NAME"));

        this.entries = db.getCollection("alpacas_manager", Entry.class);
        this.guilds = db.getCollection("guild_settings", GuildSettings.class);
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
            final GuildSettings newSettings = new GuildSettings(guildID);
            this.guilds.insertOne(newSettings);

            return newSettings;
        }

        return settings;
    }

    @Override
    public void setGuildSettings(final GuildSettings settings) {
        this.guilds.replaceOne(eq("_id", settings.getGuildID()), settings);
    }

    @Override
    public long getEntries() {
        return this.entries.countDocuments();
    }
}
