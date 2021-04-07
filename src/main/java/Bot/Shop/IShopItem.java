package Bot.Shop;

import Bot.Utils.Stat;

public interface IShopItem {

    int getPrice();

    int getSaturation();

    String getName();

    Stat getStat();
}
