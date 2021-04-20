package Bot.Shop;

import Bot.Utils.Stat;

public class Item {
	private final int price;
	private final int saturation;
	private final Stat stat;
	private final String name;

	public Item(String name, int price, int saturation, Stat stat) {
		this.name = name;
		this.price = price;
		this.saturation = saturation;
		this.stat = stat;
	}

	public String getName() {
		return this.name;
	}

	public int getPrice() {
		return this.price;
	}

	public int getSaturation() {
		return this.saturation;
	}

	public Stat getStat() {
		return this.stat;
	}
}
