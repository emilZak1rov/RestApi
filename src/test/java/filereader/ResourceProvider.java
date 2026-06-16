package filereader;

import models.Config;

public class ResourceProvider {
    private static Config config;
    private static final String CONFIG_PATH = "config.json";

    public static Config getConfig() {
        if (config == null) {
            config = FileDataReader.readAndParse(CONFIG_PATH, Config.class);
        }
        return config;
    }
}