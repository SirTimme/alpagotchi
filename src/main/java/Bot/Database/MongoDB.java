package Bot.Database;

import Bot.Config;
import Bot.Shop.IShopItem;
import Bot.Utils.Activity;
import Bot.Utils.Stat;
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
	public int getStat(long memberID, Stat stat) {
		Document resultDoc = alpacaCollection.find(Filters.eq("_id", memberID)).first();

		return resultDoc.get("alpaca", Document.class).getInteger(stat.getName());
	}

	@Override
	public void setStat(long memberID, Stat stat, int newValue) {
		Document resultDoc = alpacaCollection.find(Filters.eq("_id", memberID)).first();
		int oldValue = getStat(memberID, stat);

		alpacaCollection.updateOne(resultDoc, Updates.set("alpaca." + stat.getName(), oldValue + newValue));
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
	public int getInventory(long memberID, IShopItem item) {
		final Document resultDoc = alpacaCollection.find(Filters.eq("_id", memberID)).first();
		final String category = item.getStat().getName();

		return resultDoc.get("inventory", Document.class).get(category, Document.class).getInteger(item.getName());
	}

	@Override
	public void setInventory(long memberID, IShopItem item, int newAmount) {
		final Document resultDoc = alpacaCollection.find(Filters.eq("_id", memberID)).first();
		final String category = item.getStat().getName();
		final int oldAmount = getInventory(memberID, item);

		alpacaCollection.updateOne(resultDoc, Updates.set("inventory." + category + "." + item.getName(), oldAmount + newAmount));
	}

	@Override
	public long getCooldown(long memberID, Activity activity) {
		Document resultDoc = alpacaCollection.find(Filters.eq("_id", memberID)).first();
		Object result = resultDoc.get("cooldowns", Document.class).get(activity.getName());

		return result instanceof Integer ? ((Integer) result).longValue() : (Long) result;
	}

	@Override
	public void setCooldown(long memberID, Activity activity, long newValue) {
		Document resultDoc = alpacaCollection.find(Filters.eq("_id", memberID)).first();

		alpacaCollection.updateOne(resultDoc, Updates.set("cooldowns." + activity.getName(), newValue));
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

	@Override
	public long getAll() {
		return alpacaCollection.countDocuments();
	}
}
