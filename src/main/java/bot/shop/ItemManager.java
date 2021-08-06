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
import java.util.stream.Collectors;

import static bot.utils.Language.SINGULAR;

public class ItemManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemManager.class);
    private ArrayList<Item> items = new ArrayList<>();

    public ItemManager() {
        try {
            final BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/data/items.json"));
            final Type type = new TypeToken<ArrayList<Item>>() {}.getType();

            items = new Gson().fromJson(reader, type);
        } catch (IOException error) {
            LOGGER.error(error.getMessage());
        }
    }

    public List<Item> getItems(String stat) {
        return items.stream().filter(entry -> entry.getStat().equals(stat)).collect(Collectors.toList());
    }

    @Nullable
    public Item getItem(String search) {
        return items.stream().filter(item -> item.getName(SINGULAR).equals(search)).findAny().orElse(null);
    }
}
