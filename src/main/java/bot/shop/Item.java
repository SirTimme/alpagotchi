package bot.shop;

public class Item {
    private final int price;
    private final int saturation;
    private final String type;
    private final String name;

    public Item(final String name, final int price, final int saturation, final String type) {
        this.name = name;
        this.price = price;
        this.saturation = saturation;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public int getPrice() {
        return this.price;
    }

    public int getSaturation() {
        return this.saturation;
    }

    public String getType() {
        return this.type;
    }
}