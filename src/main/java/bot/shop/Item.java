package bot.shop;

public final class Item {
    private final String name;
    private final int price;
    private final int saturation;
    private final String type;

    public Item(String name, int price, int saturation, String type) {
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