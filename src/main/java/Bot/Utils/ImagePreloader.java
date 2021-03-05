package Bot.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImagePreloader {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImagePreloader.class);
	private static final Map<String, BufferedImage> alpacaImages = new HashMap<>();

	public static void loadAllImages() {
		try {
			final File folder = new File("src/main/resources/outfits");
			for (File file : folder.listFiles()) {
				alpacaImages.put(file.getName().split("\\.")[0], ImageIO.read(file));
			}
			LOGGER.info(folder.listFiles().length + " files successfully preloaded");
		} catch (IOException error) {
			LOGGER.error(error.getMessage());
		}
	}

	public static BufferedImage getAlpacaImage(String image) {
		return alpacaImages.get(image);
	}
}
