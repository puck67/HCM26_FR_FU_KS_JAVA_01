package fa.training.database;

import java.io.IOException;
import java.util.Properties;

public final class PropertyManager {

    private static final Properties props = new Properties();

    static {
        try (var is = PropertyManager.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is == null) {
                throw new RuntimeException("db.properties not found in classpath");
            }
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load db.properties: " + e.getMessage(), e);
        }
    }

    private PropertyManager() {
    }

    public static String get(String key) {
        return props.getProperty(key);
    }

    public static String getUrl() {
        return get("db.url");
    }

    public static String getUsername() {
        return get("db.username");
    }

    public static String getPassword() {
        return get("db.password");
    }

    public static String getDriver() {
        return get("db.driver");
    }
}
