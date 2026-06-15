package filereader;

public class ResourceProvider {
    private static ConfigModel config;
    private static final String CONFIG_PATH = "config.json";

    public static ConfigModel getConfig() {
        if (config == null) {
            config = FileDataReader.readAndParse(CONFIG_PATH, ConfigModel.class);
        }
        return config;
    }
}