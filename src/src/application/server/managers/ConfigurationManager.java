package application.server.managers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigurationManager {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T getConfig(Class<T> configClass, String filePath) {
        try {
            String json = new String(Files.readAllBytes(Paths.get(filePath)));
            return objectMapper.readValue(json, configClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
