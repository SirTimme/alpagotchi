package Bot.Shop;

public class Salad implements IShopItem {
    @Override
    public int getItemValue() {
        return 10;
    }

    @Override
    public String getItemDescription() {
        return "A green salad, nothing special (Hunger + 10)";
    }

    @Override
    public String getItemName() {
        return "salad";
    }

    @Override
    public String getItemCategory() {
        return "hunger";
    }
}
