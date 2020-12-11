package Bot.Shop;

public class Waterbottle implements IShopItem {

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
        return "A refreshing bottle of water (Thirst + 10)";
    }

    @Override
    public String getName() {
        return "waterbottle";
    }

    @Override
    public String getCategory() {
        return "thirst";
    }
}
