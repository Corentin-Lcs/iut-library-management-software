package application.server.configs;

import java.util.Objects;

public record DatabaseConfig(
    String databaseHost,
    int databasePort,
    String databaseUser,
    String databasePassword,
    String databaseName
) {
    private static final String DATABASE_CONFIG_FILE_PATH = "./config/database_config.json";

    public DatabaseConfig {
        Objects.requireNonNull(databaseHost, "The database host must not be null");
        Objects.requireNonNull(databaseName, "The database name must not be null");
    }

    public static String getDatabaseConfigFilePath() {
        return DATABASE_CONFIG_FILE_PATH;
    }
}
