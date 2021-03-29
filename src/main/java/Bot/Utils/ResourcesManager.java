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

	public static void preloadData() {
		final File messageFolder = new File("src/main/resources/answers");
		try {
			for (File file : messageFolder.listFiles()) {
				final Path filePath = Path.of(file.getPath());

				final String fileContent = Files.readString(filePath);
				final JSONArray messages = new JSONArray(fileContent);

				final String key = file.getName().split("\\.")[0];

				jsonFiles.put(key, messages);
			}
			LOGGER.info("JSON Files successfully preloaded");
		}
		catch (IOException error) {
			LOGGER.error(error.getMessage());
		}

		final File outfitFolder = new File("src/main/resources/outfits");
		try {
			for (File file : outfitFolder.listFiles()) {
				final BufferedImage image = ImageIO.read(file);
				final String key = file.getName().split("\\.")[0];

				alpacaImages.put(key, image);
			}
			LOGGER.info("Images successfully preloaded");
		}
		catch (IOException error) {
			LOGGER.error(error.getMessage());
		}
	}

	public static BufferedImage getAlpacaImage(String image) {
		return alpacaImages.get(image);
	}

	public static String getRandomMessage(String key) {
		final JSONArray array = jsonFiles.get(key);
		final int index = (int) (Math.random() * array.length());

		return array.getString(index);
	}
}
