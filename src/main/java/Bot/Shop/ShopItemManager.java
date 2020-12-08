package Bot.Shop;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ShopItemManager {
    private final List<IShopItem> shopItems = new ArrayList<>();

    public ShopItemManager() {
        addShopItem(new Salad());
        addShopItem(new Waterbottle());
        addShopItem(new Battery());
    }

    public List<IShopItem> getShopItems() {
        return this.shopItems;
    }

    @Nullable
    public IShopItem getShopItem(String search) {
        String searchLower = search.toLowerCase();

        for (IShopItem item : this.shopItems) {
            if (item.getItemName().equals(searchLower)) {
                return item;
            }
        }
        return null;
    }

    private void addShopItem(IShopItem item) {
        boolean nameFound = this.shopItems.stream().anyMatch(shopItem -> shopItem.getItemName().equalsIgnoreCase(item.getItemName()));

        if (nameFound) {
            throw new IllegalArgumentException("A shop item with this name already exists!");
        }
        shopItems.add(item);
    }
}
