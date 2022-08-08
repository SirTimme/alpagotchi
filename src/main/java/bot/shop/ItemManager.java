package bot.shop;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class ItemManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemManager.class);
    private List<Item> items;

    public ItemManager() {
        try {
            final BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/data/items.json"));
            final Type type = new TypeToken<List<Item>>() {
            }.getType();

            this.items = new Gson().fromJson(reader, type);
        } catch (final IOException error) {
            LOGGER.error(error.getMessage());
        }
    }

    public Item getItem(final String name) {
        return this.items.stream()
                         .filter(item -> item.name().equals(name))
                         .findAny()
                         .orElse(null);
    }

    public List<Item> getItems() {
        return this.items;
    }
}