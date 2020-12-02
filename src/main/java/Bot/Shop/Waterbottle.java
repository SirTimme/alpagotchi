package Bot.Shop;

public class Waterbottle implements IShopItem {
    @Override
    public int getItemValue() {
        return 10;
    }

    @Override
    public String getItemDescription() {
        return "A refreshing bottle of water (Thirst + 10)";
    }

    @Override
    public String getItemName() {
        return "waterbottle";
    }

    @Override
    public String getItemCategory() {
        return "thirst";
    }
}
