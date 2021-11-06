package bot.db;

import bot.models.Alpaca;
import bot.models.Cooldown;
import bot.models.Entry;
import bot.models.Inventory;
import bot.utils.Env;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.HashMap;

import static com.mongodb.client.model.Filters.*;

public class MongoDB implements IDatabase {
	private final MongoCollection<Document> users;

	public MongoDB() {
		final MongoClient client = MongoClients.create(Env.get("DB_URI"));
		final MongoDatabase db = client.getDatabase(Env.get("DB_NAME"));

		users = db.getCollection("alpacas_manager");
	}

	@Override
	public Entry getUser(final long memberID) {
		final Document result = users.find(eq("_id", memberID)).first();

		if (result == null) {
			return null;
		}

		return new Entry(
				result.getLong("_id"),
				docToAlpaca((Document) result.get("alpaca")),
				docToCooldown((Document) result.get("cooldown")),
				docToInventory((Document) result.get("inventory"))
		);
	}

	@Override
	public void updateUser(final Entry entry) {
		final Document updated = new Document()
				.append("_id", entry.getMemberID())
				.append("alpaca", new Document()
						.append("nickname", entry.getNickname())
						.append("outfit", entry.getOutfit())
						.append("hunger", entry.getHunger())
						.append("thirst", entry.getThirst())
						.append("energy", entry.getEnergy())
						.append("joy", entry.getJoy())
				)
				.append("inventory", new Document()
						.append("currency", entry.getCurrency())
						.append("items", new Document()
								.append("salad", entry.getItem("salad"))
								.append("taco", entry.getItem("taco"))
								.append("steak", entry.getItem("steak"))
								.append("water", entry.getItem("water"))
								.append("lemonade", entry.getItem("lemonade"))
								.append("cacao", entry.getItem("cacao"))
						)
				)
				.append("cooldown", new Document()
						.append("work", entry.getWork())
						.append("sleep", entry.getSleep())
				);

		users.replaceOne(eq("_id", entry.getMemberID()), updated);
	}

	@Override
	public void createUser(final long memberID) {
		users.insertOne(createDoc(memberID));
	}

	@Override
	public void deleteUser(final long memberID) {
		users.deleteOne(eq("_id", memberID));
	}

	@Override
	public long getEntries() {
		return users.countDocuments();
	}

	private Document createDoc(final long memberID) {
		return new Document()
				.append("_id", memberID)
				.append("alpaca", new Document()
						.append("nickname", "alpaca")
						.append("outfit", "default")
						.append("hunger", 100)
						.append("thirst", 100)
						.append("energy", 100)
						.append("joy", 100))
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
				.append("cooldown", new Document()
						.append("work", 0L)
						.append("sleep", 0L));
	}

	private Cooldown docToCooldown(final Document result) {
		return new Cooldown(
				result.getLong("work"),
				result.getLong("sleep")
		);
	}

	private Inventory docToInventory(final Document result) {
		final Document itemDoc = (Document) result.get("items");

		return new Inventory(
				result.getInteger("currency"),
				new HashMap<>() {{
					put("salad", itemDoc.getInteger("salad"));
					put("taco", itemDoc.getInteger("taco"));
					put("steak", itemDoc.getInteger("steak"));
					put("water", itemDoc.getInteger("water"));
					put("lemonade", itemDoc.getInteger("lemonade"));
					put("cacao", itemDoc.getInteger("cacao"));
				}}
		);
	}

	private Alpaca docToAlpaca(final Document result) {
		return new Alpaca(
				result.getString("outfit"),
				result.getString("nickname"),
				result.getInteger("hunger"),
				result.getInteger("thirst"),
				result.getInteger("energy"),
				result.getInteger("joy")
		);
	}
}
