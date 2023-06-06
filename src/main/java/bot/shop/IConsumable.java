package bot.shop;

import bot.models.User;

public interface IConsumable {
    String getName();

    int getPrice();

    int getSaturation();

    String getType();

    void updateInventory(User user, int newValue);
}