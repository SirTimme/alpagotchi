package bot.models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.HashMap;

public class Inventory {
    private final HashMap<String, Integer> items;
    private int currency;

    @BsonCreator
    public Inventory(@BsonProperty(value = "currency") final int currency,
            @BsonProperty(value = "items") final HashMap<String, Integer> items
    ) {
        this.currency = currency;
        this.items = items;
    }

    public int getCurrency() {
        return this.currency;
    }

    public void setCurrency(final int currency) {
        this.currency = currency;
    }

    public HashMap<String, Integer> getItems() {
        return this.items;
    }

    public int getItemByName(final String name) {
        return this.items.get(name);
    }

    public void setItem(final String item, final int newValue) {
        this.items.replace(item, newValue);
    }
}
