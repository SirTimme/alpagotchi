package Bot.Shop;

import Bot.Utils.Stat;

public class Water implements IShopItem {
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
		return "water";
	}

	@Override
	public Stat getStat() {
		return Stat.THIRST;
	}
}
