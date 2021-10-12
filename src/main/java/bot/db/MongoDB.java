package bot.db;

import bot.utils.Env;
import bot.models.DBUser;
import com.google.gson.Gson;
import com.mongodb.client.*;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;

import static com.mongodb.client.model.Filters.*;

public class MongoDB implements IDatabase {
    private final MongoCollection<Document> users;
    private final MongoCollection<Document> guilds;

    public MongoDB() {
        final MongoClient client = MongoClients.create(Env.get("DB_URI"));
        final MongoDatabase db = client.getDatabase(Env.get("DB_NAME"));

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
        guilds.replaceOne(eq("_id", guildID), guild, new ReplaceOptions().upsert(true));
    }

    @Override
    public void deleteGuild(long guildID) {
        guilds.deleteOne(eq("_id", guildID));
    }

    @Override
    public long getEntries() {
        return users.countDocuments();
    }
}
