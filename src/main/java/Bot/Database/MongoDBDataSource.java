package Bot.Database;

import Bot.Config;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoDBDataSource implements IDataBaseManager {
   private static final Logger LOGGER = LoggerFactory.getLogger(MongoDBDataSource.class);
   private final MongoCollection<Document> alpacaCollection;
   private final MongoCollection<Document> guildCollection;

   public MongoDBDataSource() {
      MongoClient mongoClient = MongoClients.create("mongodb+srv://alpacaAdmin:" + Config.get("MONGODB") + "@alpacacluster.izknd.mongodb.net/alpagotchiDB?retryWrites=true&w=majority");
      MongoDatabase database = mongoClient.getDatabase("alpagotchiDB");

      alpacaCollection = database.getCollection("alpacas_manager");
      guildCollection = database.getCollection("guild_settings");
   }

   @Override
   public String getPrefix(long guildID) {
      Document resultDoc = guildCollection.find(Filters.eq("_id", guildID)).first();

      if (resultDoc == null) {
         Document newGuildEntry = new Document();
         newGuildEntry
                 .append("_id", guildID)
                 .append("prefix", Config.get("PREFIX"));

         guildCollection.insertOne(newGuildEntry);

         return Config.get("PREFIX");
      }

      return resultDoc.getString("prefix");
   }

   @Override
   public void setPrefix(long guildID, String newPrefix) {
      Document resultDoc = guildCollection.find(Filters.eq("_id", guildID)).first();

      if (resultDoc == null) {
         LOGGER.error("Could not found the guild " + guildID + " in the database");
         return;
      }

      guildCollection.updateOne(resultDoc, Updates.set("prefix", newPrefix));
   }

   @Override
   public String getNickname(long memberID) {
      Document resultDoc = alpacaCollection.find(Filters.eq("_id", memberID)).first();

      if (resultDoc == null) {
         createNewDBEntry(memberID);
         return "alpaca";
      }

      return resultDoc.get("alpaca", Document.class).getString("nickname");
   }

   @Override
   public void setNickname(long memberID, String nickname) {
      Document resultDoc = alpacaCollection.find(Filters.eq("_id", memberID)).first();

      if (resultDoc == null) {
         LOGGER.error("Could not found the member " + memberID + " in the database");
         return;
      }

      alpacaCollection.updateOne(resultDoc, Updates.set("alpaca.nickname", nickname));
   }

   @Override
   public int getAlpacaValues(long memberID, String column) {
      Document resultDoc = alpacaCollection.find(Filters.eq("_id", memberID)).first();

      if (resultDoc == null) {
         createNewDBEntry(memberID);
         return 100;
      }

      return resultDoc.get("alpaca", Document.class).getInteger(column);
   }

   @Override
   public void setAlpacaValues(long memberID, String column, int newValue) {
      Document resultDoc = alpacaCollection.find(Filters.eq("_id", memberID)).first();

      if (resultDoc == null) {
         LOGGER.error("Could not found the member " + memberID + " in the database");
         return;
      }

      int oldValue = getAlpacaValues(memberID, column);
      alpacaCollection.updateOne(resultDoc, Updates.set("alpaca." + column, oldValue + newValue));
   }

   @Override
   public int getInventory(long memberID, String column) {
      Document resultDoc = alpacaCollection.find(Filters.eq("_id", memberID)).first();

      if (resultDoc == null) {
         createNewDBEntry(memberID);
         return 0;
      }

      return resultDoc.get("inventory", Document.class).getInteger(column);
   }

   @Override
   public void setInventory(long memberID, String column, int newValue) {
      Document resultDoc = alpacaCollection.find(Filters.eq("_id", memberID)).first();

      if (resultDoc == null) {
         LOGGER.error("Could not found the member " + memberID + " in the database");
         return;
      }

      int oldValue = resultDoc.get("inventory", Document.class).getInteger(column);
      alpacaCollection.updateOne(resultDoc, Updates.set("inventory." + column, oldValue + newValue));
   }

   @Override
   public long getCooldown(long memberID, String column) {
      Document resultDoc = alpacaCollection.find(Filters.eq("_id", memberID)).first();

      if (resultDoc == null) {
         createNewDBEntry(memberID);
         return 0L;
      }

      Object obj = resultDoc.get("cooldowns", Document.class).get(column);

      if (obj instanceof Integer) {
         return ((Integer) obj).longValue();

      } else if (obj instanceof Long) {
         return (Long) obj;

      } else {
         LOGGER.error("Could not cast cooldown");
      }
      return resultDoc.get("cooldowns", Document.class).getLong(column);
   }

   @Override
   public void setCooldown(long memberID, String column, long newValue) {
      Document resultDoc = alpacaCollection.find(Filters.eq("_id", memberID)).first();

      if (resultDoc == null) {
         LOGGER.error("Could not found the member " + memberID + " in the database");
         return;
      }

      alpacaCollection.updateOne(resultDoc, Updates.set("cooldowns." + column, newValue));
   }

   @Override
   public void decreaseValues() {
      alpacaCollection.updateMany(Filters.gte("alpaca.hunger", 2), Updates.inc("alpaca.hunger", -1));
      alpacaCollection.updateMany(Filters.gte("alpaca.thirst", 2), Updates.inc("alpaca.thirst", -1));
      alpacaCollection.updateMany(Filters.gte("alpaca.energy", 2), Updates.inc("alpaca.energy", -1));
      alpacaCollection.updateMany(Filters.gte("alpaca.joy", 2), Updates.inc("alpaca.joy", -1));
   }

   private void createNewDBEntry(long memberID) {
      Document newEntry = new Document();
      newEntry
              .append("_id", memberID)
              .append("alpaca", new Document("nickname", "alpaca")
                      .append("hunger", 100)
                      .append("thirst", 100)
                      .append("energy", 100)
                      .append("joy", 100))
              .append("inventory", new Document("currency", 0)
                      .append("salad", 0)
                      .append("waterbottle", 0))
              .append("cooldowns", new Document("work", 0)
                      .append("sleep", 0));

      alpacaCollection.insertOne(newEntry);
   }
}
