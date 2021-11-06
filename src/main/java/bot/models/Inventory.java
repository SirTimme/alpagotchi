package bot.models;

import java.util.HashMap;

public class Inventory {
    private int currency;
    private final HashMap<String, Integer> items;

    public Inventory(int currency, HashMap<String, Integer> items) {
        this.currency = currency;
        this.items = items;
    }

    public int getCurrency() {
        return this.currency;
    }

    public int getItem(String name) {
        return items.get(name);
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public void setItem(String item, int newValue) {
        items.replace(item, newValue);
    }
}
