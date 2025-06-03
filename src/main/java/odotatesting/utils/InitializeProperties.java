package odotatesting.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class InitializeProperties {

    private static final String CONFIG_FILE_PATH = "./src/test/resources/config/config.properties";

    private InitializeProperties() {
    }

    public static Properties loadProperties() {
        Properties props = new Properties();
        try(FileInputStream input = new FileInputStream(CONFIG_FILE_PATH)) {
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Could not load application properties. Check " + CONFIG_FILE_PATH, e);
        }
        return props;
    }
}
