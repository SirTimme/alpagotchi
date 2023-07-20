package bot.shop.foods;

import bot.shop.IConsumable;

public class Salad implements IConsumable {
    @Override
    public String getName() {
        return "salad";
    }

    @Override
    public int getPrice() {
        return 10;
    }

    @Override
    public int getSaturation() {
        return 10;
    }

    @Override
    public String getType() {
        return "food";
    }
}