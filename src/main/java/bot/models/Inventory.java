package bot.models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.HashMap;

public class Inventory {
	private int currency;
	private HashMap<String, Integer> items;

	@BsonCreator
	public Inventory(@BsonProperty(value = "currency") final int currency,
					 @BsonProperty(value = "items") final HashMap<String, Integer> items
	) {
		this.currency = currency;
		this.items = items;
	}

	public int getCurrency() {
		return this.currency;
	}

	public HashMap<String, Integer> getItems() {
		return this.items;
	}

	public int getItem(String name) {
		return items.get(name);
	}

	public void setCurrency(int currency) {
		this.currency = currency;
	}

	public void setItem(String item, int newValue) {
		items.replace(item, newValue);
	}
}
