package bot.shop.drinks;

import bot.shop.IConsumable;

public class Water implements IConsumable {
    @Override
    public String getName() {
        return "water";
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
        return "drink";
    }
}