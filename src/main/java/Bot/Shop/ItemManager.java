package Bot.Shop;

import Bot.Utils.Stat;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemManager.class);
	private final List<Item> items = new ArrayList<>();

	public ItemManager() {
		try {
			final File file = new File("src/main/resources/data/items.json");
			final Path filePath = Path.of(file.getPath());
			final String content = Files.readString(filePath);

			final JSONObject json = new JSONObject(content);
			for (String key : json.keySet()) {
				JSONObject object = json.getJSONObject(key);
				Item item = new Item(key, object.getInt("price"), object.getInt("saturation"), Stat.valueOf(object.getString("stat").toUpperCase()));

				items.add(item);
			}
		}
		catch (IOException error) {
			LOGGER.error(error.getMessage());
		}
	}

	public List<Item> getItems() {
		return items;
	}

	public List<Item> getItems(Stat stat) {
		return items.stream().filter(item -> item.getStat().equals(stat)).collect(Collectors.toList());
	}

	@Nullable
	public Item getItem(String search) {
		for (Item item : items) {
			if (item.getName().equals(search)) {
				return item;
			}
		}
		return null;
	}
}
