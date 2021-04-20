package Bot.Dresses;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DressManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(DressManager.class);
	private final List<Dress> dresses = new ArrayList<>();

	public DressManager() {
		try {
			final File file = new File("src/main/resources/data/outfits.json");
			final Path filePath = Path.of(file.getPath());
			final String content = Files.readString(filePath);

			final JSONObject json = new JSONObject(content);
			for (String key : json.keySet()) {
				JSONObject object = json.getJSONObject(key);
				Dress dress = new Dress(key, object.getString("description"));

				dresses.add(dress);
			}
		}
		catch (IOException error) {
			LOGGER.error(error.getMessage());
		}
	}

	public List<Dress> getOutfits() {
		return this.dresses;
	}

	public Dress getOutfit(String search) {
		for (Dress dress : this.dresses) {
			if (dress.getName().equals(search)) {
				return dress;
			}
		}
		return null;
	}
}
