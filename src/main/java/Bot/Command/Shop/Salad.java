package Bot.Command.Shop;

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
}
