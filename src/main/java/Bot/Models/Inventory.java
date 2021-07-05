package Bot.Models;

import com.mongodb.lang.Nullable;

import java.util.Map;

public class Inventory {
    private final int currency;
    private final Map<String, Integer> items;

    public Inventory(int currency, Map<String, Integer> items) {
        this.currency = currency;
        this.items = items;
    }

    public int getCurrency() {
        return currency;
    }

    @Nullable
    public int getItem(String name) {
        return items.get(name);
    }
}
