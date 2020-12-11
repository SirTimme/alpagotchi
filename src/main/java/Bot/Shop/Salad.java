package Bot.Shop;

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
    public String getDescription() {
        return "A green salad, nothing special (Hunger + 10)";
    }

    @Override
    public String getName() {
        return "salad";
    }

    @Override
    public String getCategory() {
        return "hunger";
    }
}
