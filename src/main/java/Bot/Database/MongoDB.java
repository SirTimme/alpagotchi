package Bot.Database;

import Bot.Config;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;

@SuppressWarnings("ConstantConditions")
public class MongoDB implements IDatabase {
	private final MongoCollection<Document> alpacaCollection;
	private final MongoCollection<Document> guildCollection;

	public MongoDB() {
		MongoClient mongoClient = MongoClients.create(Config.get("DB_URI"));
		MongoDatabase database = mongoClient.getDatabase("alpagotchiDB");

		alpacaCollection = database.getCollection("alpacas_manager");
		guildCollection = database.getCollection("guild_settings");
	}

	@Override
	public String getPrefix(long guildID) {
		Document resultDoc = guildCollection.find(Filters.eq("_id", guildID)).first();

		return resultDoc.getString("prefix");
	}

	@Override
	public void setPrefix(long guildID, String newPrefix) {
		Document resultDoc = guildCollection.find(Filters.eq("_id", guildID)).first();

		guildCollection.updateOne(resultDoc, Updates.set("prefix", newPrefix));
	}

	@Override
	public String getNickname(long memberID) {
		Document resultDoc = alpacaCollection.find(Filters.eq("_id", memberID)).first();

		return resultDoc.get("alpaca", Document.class).getString("nickname");
	}

	@Override
	public void setNickname(long memberID, String newNickname) {
		Document resultDoc = alpacaCollection.find(Filters.eq("_id", memberID)).first();

		alpacaCollection.updateOne(resultDoc, Updates.set("alpaca.nickname", newNickname));
	}

	@Override
	public String getOutfit(long memberID) {
		Document resultDoc = alpacaCollection.find(Filters.eq("_id", memberID)).first();

		return resultDoc.get("alpaca", Document.class).getString("outfit");
	}

	@Override
	public void setOutfit(long memberID, String newOutfit) {
		Document resultDoc = alpacaCollection.find(Filters.eq("_id", memberID)).first();

		alpacaCollection.updateOne(resultDoc, Updates.set("alpaca.outfit", newOutfit));
	}

	@Override
	public int getAlpacaValues(long memberID, String column) {
		Document resultDoc = alpacaCollection.find(Filters.eq("_id", memberID)).first();

		return resultDoc.get("alpaca", Document.class).getInteger(column);
	}

	@Override
	public void setAlpacaValues(long memberID, String column, int newValue) {
		Document resultDoc = alpacaCollection.find(Filters.eq("_id", memberID)).first();
		int oldValue = getAlpacaValues(memberID, column);

		alpacaCollection.updateOne(resultDoc, Updates.set("alpaca." + column, oldValue + newValue));
	}

	@Override
	public int getBalance(long memberID) {
		Document resultDoc = alpacaCollection.find(Filters.eq("_id", memberID)).first();

		return resultDoc.get("inventory", Document.class).getInteger("currency");
	}

	@Override
	public void setBalance(long memberID, int newBalance) {
		Document resultDoc = alpacaCollection.find(Filters.eq("_id", memberID)).first();
		int oldBalance = getBalance(memberID);

		alpacaCollection.updateOne(resultDoc, Updates.set("inventory.currency", oldBalance + newBalance));
	}

	@Override
	public int getInventory(long memberID, String category, String item) {
		Document resultDoc = alpacaCollection.find(Filters.eq("_id", memberID)).first();

		return resultDoc.get("inventory", Document.class).get(category, Document.class).getInteger(item);
	}

	@Override
	public void setInventory(long memberID, String category, String item, int newAmount) {
		Document resultDoc = alpacaCollection.find(Filters.eq("_id", memberID)).first();
		int oldAmount = getInventory(memberID, category, item);

		alpacaCollection.updateOne(resultDoc, Updates.set("inventory." + category + "." + item, oldAmount + newAmount));
	}

	@Override
	public long getCooldown(long memberID, String column) {
		Document resultDoc = alpacaCollection.find(Filters.eq("_id", memberID)).first();
		Object result = resultDoc.get("cooldowns", Document.class).get(column);

		return result instanceof Integer ? ((Integer) result).longValue() : (Long) result;
	}

	@Override
	public void setCooldown(long memberID, String column, long newValue) {
		Document resultDoc = alpacaCollection.find(Filters.eq("_id", memberID)).first();

		alpacaCollection.updateOne(resultDoc, Updates.set("cooldowns." + column, newValue));
	}

	@Override
	public void createUserEntry(long memberID) {
		Document newUser = new Document();
		newUser.append("_id", memberID)
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
				   .append("hunger", new Document()
					   .append("salad", 0)
					   .append("taco", 0)
					   .append("steak", 0)
				   )
				   .append("thirst", new Document()
					   .append("water", 0)
					   .append("lemonade", 0)
					   .append("cacao", 0)
				   )
			   )
			   .append("cooldowns", new Document()
				   .append("work", 0)
				   .append("sleep", 0)
			   );

		alpacaCollection.insertOne(newUser);
	}

	@Override
	public void deleteUserEntry(long memberID) {
		alpacaCollection.deleteOne(Filters.eq("_id", memberID));
	}

	@Override
	public void createGuildEntry(long guildID) {
		Document newGuild = new Document();
		newGuild.append("_id", guildID)
				.append("prefix", Config.get("PREFIX"));

		guildCollection.replaceOne(Filters.eq("_id", guildID), newGuild, new ReplaceOptions().upsert(true));
	}

	@Override
	public void deleteGuildEntry(long guildID) {
		guildCollection.deleteOne(Filters.eq("_id", guildID));
	}

	@Override
	public boolean isUserInDB(long memberID) {
		return alpacaCollection.find(Filters.eq("_id", memberID)).first() != null;
	}

	@Override
	public void decreaseValues() {
		alpacaCollection.updateMany(Filters.gte("alpaca.hunger", 2), Updates.inc("alpaca.hunger", -1));
		alpacaCollection.updateMany(Filters.gte("alpaca.thirst", 2), Updates.inc("alpaca.thirst", -1));
	}
}
