package Bot.Database;

import Bot.Config;
import Bot.Models.Alpaca;
import Bot.Models.Cooldowns;
import Bot.Models.Entry;
import Bot.Models.Inventory;
import Bot.Utils.Stat;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadConcern;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.connection.ConnectionPoolSettings;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MongoDB implements IDatabase {
    private final MongoCollection<Document> users;
    private final MongoCollection<Document> guilds;

    public MongoDB() {
        ConnectionPoolSettings pool = ConnectionPoolSettings.builder()
                                                            .maxConnectionIdleTime(1, TimeUnit.HOURS)
                                                            .maxConnectionLifeTime(2, TimeUnit.HOURS)
                                                            .maintenanceInitialDelay(1, TimeUnit.HOURS)
                                                            .maintenanceFrequency(2, TimeUnit.HOURS)
                                                            .build();

        ConnectionString connString = new ConnectionString(Config.get("DB_URI"));

        MongoClientSettings settings = MongoClientSettings.builder()
                                                          .applyToConnectionPoolSettings(builder -> builder.applySettings(pool))
                                                          .applyConnectionString(connString)
                                                          .retryWrites(true)
                                                          .readConcern(ReadConcern.MAJORITY)
                                                          .build();

        MongoClient client = MongoClients.create(settings);
        MongoDatabase database = client.getDatabase(Config.get("DB_NAME"));

        users = database.getCollection("alpacas_manager");
        guilds = database.getCollection("guild_settings");
    }

    @Override
    public String getPrefix(long guildID) {
        return guilds.find(Filters.eq("_id", guildID)).first().getString("prefix");
    }

    @Override
    public Entry getEntry(long memberID) {
        Document result = users.find(Filters.eq("_id", memberID)).first();

        if (result == null) {
            return null;
        }

        Document alpaca = result.get("alpaca", Document.class);
        Document cooldowns = result.get("cooldowns", Document.class);
        Document inventory = result.get("inventory", Document.class);
        Document shopItems = inventory.get("items", Document.class);

        Map<String, Integer> items = new HashMap<>();
        shopItems.keySet().forEach((item -> items.put(item, shopItems.getInteger(item))));

        return new Entry(
                new Alpaca(
                        alpaca.getString("outfit"),
                        alpaca.getString("nickname"),
                        alpaca.getInteger("hunger"),
                        alpaca.getInteger("thirst"),
                        alpaca.getInteger("energy"),
                        alpaca.getInteger("joy")
                ),
                new Cooldowns(
                        cooldowns.getLong("sleep"),
                        cooldowns.getLong("work")
                ),
                new Inventory(
                        inventory.getInteger("currency"),
                        items
                )
        );
    }

    @Override
    public <T> void setEntry(long memberID, Stat stat, T newValue) {
        Document doc = users.find(Filters.eq("_id", memberID)).first();
        Number oldValue;

        switch (stat) {
            case JOY:
            case ENERGY:
            case HUNGER:
            case THIRST:
                oldValue = doc.get("alpaca", Document.class).getInteger(stat.getName());
                users.updateOne(doc, Updates.set("alpaca." + stat.getName(), oldValue.intValue() + (int) newValue));
                break;
            case CURRENCY:
                oldValue = doc.get("inventory", Document.class).getInteger(stat.getName());
                users.updateOne(doc, Updates.set("inventory." + stat.getName(), oldValue.intValue() + (int) newValue));
                break;
            case WORK:
            case SLEEP:
                users.updateOne(doc, Updates.set("cooldowns." + stat.getName(), (long) newValue));
                break;
            case NICKNAME:
            case OUTFIT:
                users.updateOne(doc, Updates.set("alpaca." + stat.getName(), newValue.toString()));
                break;
            case SALAD:
            case TACO:
            case STEAK:
            case WATER:
            case LEMONADE:
            case CACAO:
                oldValue = doc.get("inventory", Document.class).get("items", Document.class).getInteger(stat.getName());
                users.updateOne(doc, Updates.set("inventory.items." + stat.getName(), oldValue.intValue() + (int) newValue));
                break;
            default:
        }
    }

    @Override
    public void createEntry(long memberID) {
        Document user = new Document();

        user.append("_id", memberID)
            .append("alpaca", new Document()
                    .append("nickname", "alpaca")
                    .append("hunger", 100)
                    .append("thirst", 100)
                    .append("energy", 100)
                    .append("joy", 100)
                    .append("outfit", "default")
            )
            .append("inventory", new Document()
                    .append("currency", 0)
                    .append("items", new Document()
                            .append("salad", 0)
                            .append("taco", 0)
                            .append("steak", 0)
                            .append("water", 0)
                            .append("lemonade", 0)
                            .append("cacao", 0)
                    )
            )
            .append("cooldowns", new Document()
                    .append("work", 0L)
                    .append("sleep", 0L)
            );

        users.insertOne(user);
    }

    @Override
    public void deleteEntry(long memberID) {
        users.deleteOne(Filters.eq("_id", memberID));
    }

    @Override
    public void createGuild(long guildID) {
        Document guild = new Document();

        guild.append("_id", guildID)
             .append("prefix", Config.get("PREFIX"));

        guilds.replaceOne(Filters.eq("_id", guildID), guild, new ReplaceOptions().upsert(true));
    }

    @Override
    public void deleteGuild(long guildID) {
        guilds.deleteOne(Filters.eq("_id", guildID));
    }

    @Override
    public void decreaseValues() {
        users.updateMany(Filters.gte("alpaca.hunger", 2), Updates.inc("alpaca.hunger", -1));
        users.updateMany(Filters.gte("alpaca.thirst", 2), Updates.inc("alpaca.thirst", -1));
    }

    @Override
    public long getEntries() {
        return users.countDocuments();
    }
}
