package Bot.Database;

import Bot.Config;
import Bot.Models.DBUser;
import com.google.gson.Gson;
import com.mongodb.client.*;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import static com.mongodb.client.model.Filters.*;

public class MongoDB implements IDatabase {
    private final MongoCollection<Document> users;
    private final MongoCollection<Document> guilds;

    public MongoDB() {
        final MongoClient client = MongoClients.create(Config.get("DB_URI"));
        final MongoDatabase db = client.getDatabase(Config.get("DB_NAME"));

        users = db.getCollection("alpacas_manager");
        guilds = db.getCollection("guild_settings");
    }

    @Override
    public DBUser getUser(long memberID) {
        final Document result = users.find(eq("_id", memberID)).first();
        if (result == null) {
            return null;
        }
        return new Gson().fromJson(result.toJson(), DBUser.class);
    }

    @Override
    public void setUser(long memberID, DBUser DBUser) {
        final Document updated = Document.parse(new Gson().toJson(DBUser));
        users.replaceOne(eq("_id", memberID), updated);
    }

    @Override
    public void createUser(long memberID) {
        final Document user = Document.parse(new Gson().toJson(new DBUser(memberID)));
        users.insertOne(user);
    }

    @Override
    public void deleteUser(long memberID) {
        users.deleteOne(eq("_id", memberID));
    }

    @Override
    public void createGuild(long guildID) {
        final Document guild = new Document().append("_id", guildID);
        guilds.insertOne(guild);
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
