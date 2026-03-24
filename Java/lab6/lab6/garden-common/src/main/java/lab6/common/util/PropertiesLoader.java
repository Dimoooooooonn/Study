package lab6.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesLoader {
    private PropertiesLoader() {
    }

    public static Properties load(String resourceName) {
        Properties properties = new Properties();
        try (InputStream inputStream = PropertiesLoader.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException ignored) {
        }
        return properties;
    }
}
