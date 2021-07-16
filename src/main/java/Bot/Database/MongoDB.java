package Bot.Database;

import Bot.Config;
import Bot.Models.Entry;
import com.google.gson.Gson;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import static com.mongodb.client.model.Filters.*;

public class MongoDB implements IDatabase {
    private final MongoCollection<Document> users;
    private final MongoCollection<Document> guilds;

    public MongoDB() {
        ConnectionString connection = new ConnectionString(Config.get("DB_URI"));
        MongoClientSettings settings = MongoClientSettings.builder()
                                                          .applyConnectionString(connection)
                                                          .build();

        MongoClient client = MongoClients.create(settings);
        MongoDatabase database = client.getDatabase(Config.get("DB_NAME"));

        users = database.getCollection("alpacas_manager");
        guilds = database.getCollection("guild_settings");
    }

    @Override
    public String getPrefix(long guildID) {
        return guilds.find(eq("_id", guildID)).first().getString("prefix");
    }

    @Override
    public Entry getEntry(long memberID) {
        Document result = users.find(eq("_id", memberID)).first();

        if (result == null) {
            return null;
        }
        Gson gson = new Gson();

        return gson.fromJson(result.toJson(), Entry.class);
    }

    @Override
    public void setEntry(long memberID, Entry entry) {
        Gson gson = new Gson();
        Document updated = Document.parse(gson.toJson(entry));

        users.replaceOne(eq("_id", memberID), updated);
    }

    @Override
    public void createEntry(long memberID) {
        Gson gson = new Gson();
        Document user = Document.parse(gson.toJson(new Entry()));
        user.append("_id", memberID);

        users.insertOne(user);
    }

    @Override
    public void deleteEntry(long memberID) {
        users.deleteOne(eq("_id", memberID));
    }

    @Override
    public void createGuild(long guildID) {
        Document guild = new Document();

        guild.append("_id", guildID)
             .append("prefix", Config.get("PREFIX"));

        guilds.replaceOne(eq("_id", guildID), guild, new ReplaceOptions().upsert(true));
    }

    @Override
    public void deleteGuild(long guildID) {
        guilds.deleteOne(eq("_id", guildID));
    }

    @Override
    public void decreaseValues() {
        users.updateMany(gte("alpaca.hunger", 2), Updates.inc("alpaca.hunger", -1));
        users.updateMany(gte("alpaca.thirst", 2), Updates.inc("alpaca.thirst", -1));
    }

    @Override
    public long getEntries() {
        return users.countDocuments();
    }
}
