package Bot.Database;

import Bot.Config;
import Bot.Shop.Item;
import Bot.Utils.Stat;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;

public class MongoDB implements IDatabase {
	private final MongoCollection<Document> users;
	private final MongoCollection<Document> guilds;

	public MongoDB() {
		MongoClient client = MongoClients.create(Config.get("DB_URI"));
		MongoDatabase database = client.getDatabase(Config.get("DB_NAME"));

		users = database.getCollection("alpacas_manager");
		guilds = database.getCollection("guild_settings");
	}

	@Override
	public String getPrefix(long guildID) {
		return getGuild(guildID).getString("prefix");
	}

	@Override
	public void setPrefix(long guildID, String newPrefix) {
		guilds.updateOne(getGuild(guildID), Updates.set("prefix", newPrefix));
	}

	@Override
	public int getStatInt(long memberID, Stat stat) {
		switch (stat) {
			case JOY:
			case ENERGY:
			case HUNGER:
			case THIRST:
				return getUser(memberID).get("alpaca", Document.class).getInteger(stat.getName());
			case CURRENCY:
				return getUser(memberID).get("inventory", Document.class).getInteger(stat.getName());
			default:
				return 0;
		}
	}

	@Override
	public void setStatInt(long memberID, Stat stat, int newValue) {
		int oldValue = getStatInt(memberID, stat);
		users.updateOne(getUser(memberID), Updates.set("alpaca." + stat.getName(), oldValue + newValue));
	}

	@Override
	public long getStatLong(long memberID, Stat stat) {
		switch (stat) {
			case SLEEP:
			case WORK:
				Object result = getUser(memberID).get("cooldowns", Document.class).get(stat.getName());
				return result instanceof Integer ? ((Integer) result).longValue() : (Long) result;
			default:
				return 0;
		}
	}

	@Override
	public void setStatLong(long memberID, Stat stat, long newValue) {
		users.updateOne(getUser(memberID), Updates.set("cooldowns." + stat.getName(), newValue));
	}

	@Override
	public String getStatString(long memberID, Stat stat) {
		switch (stat) {
			case NICKNAME:
			case OUTFIT:
				return getUser(memberID).get("alpaca", Document.class).getString(stat.getName());
			default:
				return "";
		}
	}

	@Override
	public void setStatString(long memberID, Stat stat, String newValue) {
		users.updateOne(getUser(memberID), Updates.set("alpaca." + stat.getName(), newValue));
	}

	@Override
	public int getInventory(long memberID, Item item) {
		final String category = item.getStat().getName();

		return getUser(memberID).get("inventory", Document.class).get(category, Document.class).getInteger(item.getName());
	}

	@Override
	public void setInventory(long memberID, Item item, int newAmount) {
		final String category = item.getStat().getName();
		final int oldAmount = getInventory(memberID, item);

		users.updateOne(getUser(memberID), Updates.set("inventory." + category + "." + item.getName(), oldAmount + newAmount));
	}

	@Override
	public void createUser(long memberID) {
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

		users.insertOne(user);
	}

	@Override
	public void deleteUser(long memberID) {
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

	@Override
	public Document getUser(long memberID) {
		return users.find(Filters.eq("_id", memberID)).first();
	}

	@Override
	public Document getGuild(long guildID) {
		return guilds.find(Filters.eq("_id", guildID)).first();
	}
}
