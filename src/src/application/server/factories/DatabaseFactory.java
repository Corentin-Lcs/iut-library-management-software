package application.server.factories;

import application.server.configs.DatabaseConfig;
import application.server.managers.ConfigurationManager;
import application.server.managers.DatabaseManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseFactory {
    private static final Logger LOGGER = LogManager.getLogger("Database Factory");

    public static void setupDatabase() {
        LOGGER.info("Setting up database");
        DatabaseManager.setFromConfig(ConfigurationManager.getConfig(DatabaseConfig.class, DatabaseConfig.getDatabaseConfigFilePath()));
        LOGGER.info("Database setup complete");
    }
}
