package bot.models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.HashMap;

/**
 * Represents the users inventory
 */
public class Inventory {
	private int currency;
	private final HashMap<String, Integer> items;

	/**
	 * Constructor used for serialization from db
	 * @param currency The users currency
	 * @param items The users items
	 */
	@BsonCreator
	public Inventory(@BsonProperty(value = "currency") final int currency,
					 @BsonProperty(value = "items") final HashMap<String, Integer> items
	) {
		this.currency = currency;
		this.items = items;
	}

	/**
	 * Returns the users balance
	 * @return The users currency
	 */
	public int getCurrency() {
		return this.currency;
	}

	/**
	 * Returns all the users items
	 * @return The users items
	 */
	public HashMap<String, Integer> getItems() {
		return this.items;
	}

	/**
	 * Retrieves the amount of a specific item
	 * @param name The items name
	 * @return The items amount
	 */
	public int getItemByName(String name) {
		return this.items.get(name);
	}

	/**
	 * Sets the balance of the user
	 * @param currency The new balance
	 */
	public void setCurrency(int currency) {
		this.currency = currency;
	}

	/**
	 * Sets the amount of a specific item
	 * @param item The name of the item
	 * @param newValue The new amount of the item
	 */
	public void setItem(String item, int newValue) {
		this.items.replace(item, newValue);
	}
}
