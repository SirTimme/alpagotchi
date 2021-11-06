package bot.utils;

import bot.models.Alpaca;
import bot.models.Cooldown;
import bot.models.Entry;
import bot.models.Inventory;
import org.bson.Document;

import java.util.HashMap;

public class DocumentService {
	public static Document createDocument(final long memberID) {
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

	public static Document updateDocument(final Entry entry) {
		return new Document()
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
	}

	public static Entry parseEntry(final Document doc) {
		return new Entry(
				doc.getLong("_id"),
				parseAlpaca((Document) doc.get("alpaca")),
				parseCooldown((Document) doc.get("cooldown")),
				parseInventory((Document) doc.get("inventory"))
		);
	}

	private static Cooldown parseCooldown(final Document result) {
		return new Cooldown(
				result.getLong("work"),
				result.getLong("sleep")
		);
	}

	private static Inventory parseInventory(final Document result) {
		return new Inventory(
				result.getInteger("currency"),
				parseItems((Document) result.get("items"))
		);
	}

	private static HashMap<String, Integer> parseItems(final Document result) {
		return new HashMap<>() {{
			put("salad", result.getInteger("salad"));
			put("taco", result.getInteger("taco"));
			put("steak", result.getInteger("steak"));
			put("water", result.getInteger("water"));
			put("lemonade", result.getInteger("lemonade"));
			put("cacao", result.getInteger("cacao"));
		}};
	}

	private static Alpaca parseAlpaca(final Document result) {
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
