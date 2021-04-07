package Bot.Shop;

import Bot.Utils.Stat;

public class Steak implements IShopItem {
	@Override
	public int getPrice() {
		return 40;
	}

	@Override
	public int getSaturation() {
		return 40;
	}

	@Override
	public String getName() {
		return "steak";
	}

	@Override
	public Stat getStat() {
		return Stat.HUNGER;
	}
}
