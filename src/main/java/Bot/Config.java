package Bot;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {

    private static final Dotenv dotenv = Dotenv
            .configure()
            .directory("../../../../Alpagotchi")
            .load();

    public static String get(String key) {
        return dotenv.get(key);
    }
}
