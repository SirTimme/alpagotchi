package Bot.Shop;

public class Battery implements IShopItem {

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
        return "A dense brick of electricity";
    }

    @Override
    public String getName() {
        return "battery";
    }

    @Override
    public String getCategory() {
        return "energy";
    }
}
