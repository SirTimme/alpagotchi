package Bot.Shop;

import Bot.Utils.Stat;

public class Lemonade implements IShopItem {
	@Override
	public int getPrice() {
		return 25;
	}

	@Override
	public int getSaturation() {
		return 25;
	}

	@Override
	public String getName() {
		return "lemonade";
	}

	@Override
	public Stat getStat() {
		return Stat.THIRST;
	}
}
