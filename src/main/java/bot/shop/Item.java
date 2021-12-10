package bot.shop;

/**
 * Represents one item in the shop
 */
public class Item {
	private final int price;
	private final int saturation;
	private final String stat;
	private final String name;

	/**
	 * Constructor for a new item
	 * @param name The display name of the item
	 * @param price The cost of the item
	 * @param saturation The saturation value of the item
	 * @param stat If the item affects hunger or thirst
	 */
	public Item(final String name, final int price, final int saturation, final String stat) {
		this.name = name;
		this.price = price;
		this.saturation = saturation;
		this.stat = stat;
	}

	/**
	 * Retrieves the display name of the item
	 * @return the display name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Retrieves the price of the item
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * Retrieves the saturation value of the item
	 * @return the saturation value
	 */
	public int getSaturation() {
		return saturation;
	}

	/**
	 * Retrieves the affecting stat of the item
	 * @return the affecting stat
	 */
	public String getStat() {
		return stat;
	}
}
