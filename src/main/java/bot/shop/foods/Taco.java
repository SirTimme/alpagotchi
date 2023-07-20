package bot.shop.foods;

import bot.shop.IConsumable;

public class Taco implements IConsumable {
    @Override
    public String getName() {
        return "taco";
    }

    @Override
    public int getPrice() {
        return 25;
    }

    @Override
    public int getSaturation() {
        return 25;
    }

    @Override
    public String getType() {
        return "food";
    }
}