package Bot.Shop;

public class Battery implements IShopItem {
    @Override
    public String getItemValue() {
        return "10";
    }

    @Override
    public String getItemDescription() {
        return "A dense brick of electricity (Energy + 10)";
    }

    @Override
    public String getItemName() {
        return "battery";
    }
}
