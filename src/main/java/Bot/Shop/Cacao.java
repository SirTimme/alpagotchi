package Bot.Shop;

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
	public String getCategory() {
		return "thirst";
	}
}
