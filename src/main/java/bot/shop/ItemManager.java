package bot.shop;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemManager.class); // Logs errors
    private ArrayList<Item> items; // contains all buyable items

    public ItemManager() {
        try {
            final BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/data/items.json"));
            final Type type = new TypeToken<ArrayList<Item>>() {}.getType();

            this.items = new Gson().fromJson(reader, type);
        } catch (final IOException error) {
            LOGGER.error(error.getMessage());
            this.items = new ArrayList<>();
        }
    }

    public List<Item> getItemsByStat(final String stat) {
        return this.items.stream()
                         .filter(entry -> entry.getStat().equals(stat))
                         .toList();
    }

    @Nullable
    public Item getItemByName(final String search) {
        return this.items.stream()
                         .filter(item -> item.getName().equals(search))
                         .findAny()
                         .orElse(null);
    }
}
