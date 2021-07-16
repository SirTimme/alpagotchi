package Bot.Models;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private int currency;
    private final Map<String, Integer> items;

    public Inventory() {
        this.currency = 0;
        this.items = new HashMap<>() {{
            put("salad", 0);
            put("taco", 0);
            put("steak", 0);
            put("water", 0);
            put("lemonade", 0);
            put("cacao", 0);
        }};
    }

    public int getCurrency() {
        return currency;
    }

    public int getItem(String name) {
        return items.get(name);
    }

    public void setCurrency(int currency) {
        this.currency = this.currency + currency;
    }

    public void setItem(String item, int newValue) {
        items.replace(item, items.get(item) + newValue);
    }
}
