package bot.shop;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ItemManager {
    private Map<String, IConsumable> items;

    public ItemManager() {
        this.items = new TreeMap<>();
        this.items.put("salad", new Salad());
    }

    public IConsumable getItem(final String itemName) {
        return this.items.get(itemName);
    }

    public List<IConsumable> getItems() {
        return this.items.values().stream().toList();
    }
}