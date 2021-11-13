package bot.shop;

public class Item {
	private final int price;
	private final int saturation;
	private final String stat;
	private final String name;

	public Item(final String name, final int price, final int saturation, final String stat) {
		this.name = name;
		this.price = price;
		this.saturation = saturation;
		this.stat = stat;
	}

	public String getName() {
		return this.name;
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
