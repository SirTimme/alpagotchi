package Bot.Shop;

import Bot.Utils.Language;

import static Bot.Utils.Language.SINGULAR;

public class Item {
	private final int price;
	private final int saturation;
	private final String stat;
	private final String[] names;

	public Item(String[] names, int price, int saturation, String stat) {
		this.names = names;
		this.price = price;
		this.saturation = saturation;
		this.stat = stat;
	}

	public String getName(Language number) {
		return number.equals(SINGULAR) ? names[0] : names[1];
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
