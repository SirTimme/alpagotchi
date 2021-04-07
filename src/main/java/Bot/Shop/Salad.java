package Bot.Shop;

import Bot.Utils.Stat;

public class Salad implements IShopItem {
	@Override
	public int getPrice() {
		return 10;
	}

	@Override
	public int getSaturation() {
		return 10;
	}

	@Override
	public String getName() {
		return "salad";
	}

	@Override
	public Stat getStat() {
		return Stat.HUNGER;
	}
}
