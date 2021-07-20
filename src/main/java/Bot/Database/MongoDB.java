package Bot.Database;

import Bot.Config;
import Bot.Models.User;
import com.google.gson.Gson;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadPreference;
import com.mongodb.client.*;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.connection.SocketSettings;
import org.bson.Document;

import java.util.concurrent.TimeUnit;

import static com.mongodb.client.model.Filters.*;

public class MongoDB implements IDatabase {
    private final MongoCollection<Document> users;
    private final MongoCollection<Document> guilds;

    public MongoDB() {
        ConnectionString connection = new ConnectionString(Config.get("DB_URI"));

        SocketSettings socketSettings = SocketSettings.builder()
                                                      .connectTimeout(30, TimeUnit.SECONDS)
                                                      .readTimeout(30, TimeUnit.SECONDS)
                                                      .build();

        MongoClientSettings clientSettings = MongoClientSettings.builder()
                                                                .applyToSocketSettings((setting) -> setting.applySettings(socketSettings))
                                                                .readPreference(ReadPreference.primaryPreferred())
                                                                .applyConnectionString(connection)
                                                                .build();

        MongoClient client = MongoClients.create(clientSettings);
        MongoDatabase database = client.getDatabase(Config.get("DB_NAME"));

        users = database.getCollection("alpacas_manager");
        guilds = database.getCollection("guild_settings");
    }

    @Override
    public String getPrefix(long guildID) {
        return guilds.find(eq("_id", guildID)).first().getString("prefix");
    }

    @Override
    public User getUser(long memberID) {
        Document result = users.find(eq("_id", memberID)).first();
        if (result == null) {
            return null;
        }
        Gson gson = new Gson();

        return gson.fromJson(result.toJson(), User.class);
    }

    @Override
    public void setUser(long memberID, User user) {
        Gson gson = new Gson();
        Document updated = Document.parse(gson.toJson(user));

        users.replaceOne(eq("_id", memberID), updated);
    }

    @Override
    public void createUser(long memberID) {
        Gson gson = new Gson();
        Document user = Document.parse(gson.toJson(new User(memberID)));

        users.insertOne(user);
    }

    @Override
    public void deleteUser(long memberID) {
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
