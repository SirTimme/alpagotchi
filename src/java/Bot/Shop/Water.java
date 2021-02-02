package Bot.Shop;

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
	public String getCategory() {
		return "thirst";
	}
}
