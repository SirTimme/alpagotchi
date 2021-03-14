package Bot.Utils;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ResourcesManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(ResourcesManager.class);
	private static final Map<String, BufferedImage> alpacaImages = new HashMap<>();
	private static final Map<String, JSONArray> jsonFiles = new HashMap<>();

	public static void preloadJSON() {
		final File folder = new File("src/main/resources/answers");
		try {
			for (File file : folder.listFiles()) {
				final Path filePath = Path.of(file.getPath());

				final String jsonString = Files.readString(filePath);
				final JSONArray array = new JSONArray(jsonString);

				final String key = file.getName().split("\\.")[0];

				jsonFiles.put(key, array);
			}
			LOGGER.info("JSON Files successfully preloaded");
		} catch (IOException error) {
			LOGGER.error(error.getMessage());
		}
	}

	public static void preloadImages() {
		final File folder = new File("src/main/resources/outfits");
		try {
			for (File file : folder.listFiles()) {
				final BufferedImage image = ImageIO.read(file);
				final String key = file.getName().split("\\.")[0];

				alpacaImages.put(key, image);
			}
			LOGGER.info("Images successfully preloaded");
		} catch (IOException error) {
			LOGGER.error(error.getMessage());
		}
	}

	public static BufferedImage getAlpacaImage(String image) {
		return alpacaImages.get(image);
	}

	public static String getRandomMessage(String key) {
		final JSONArray array = jsonFiles.get(key);
		final int randomIndex = (int) (Math.random() * (array.length() - 1));

		return array.getString(randomIndex);
	}
}
