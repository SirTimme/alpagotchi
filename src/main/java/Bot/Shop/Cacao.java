package Bot.Shop;

import Bot.Utils.Stat;

public class Cacao implements IShopItem {
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
		return "cacao";
	}

	@Override
	public Stat getStat() {
		return Stat.THIRST;
	}
}
