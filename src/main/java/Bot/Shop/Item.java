package Bot.Shop;

public class Item {
	private final int price;
	private final int saturation;
	private final String name, stat;

	public Item(String name, int price, int saturation, String stat) {
		this.name = name;
		this.price = price;
		this.saturation = saturation;
		this.stat = stat;
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public int getSaturation() {
		return saturation;
	}

	public String getStat() {
		return stat;
	}
}
