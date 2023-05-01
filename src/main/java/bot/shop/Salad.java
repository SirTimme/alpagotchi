package bot.shop;

import bot.models.User;

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
        return "hunger";
    }

    @Override
    public void updateItem(User user, int newValue) {
        final var oldValue = user.getInventory().getSalad();
        user.getInventory().setSalad(oldValue + newValue);
    }
}