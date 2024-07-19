package application.server.managers;

import application.server.configs.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class DatabaseManager {
    private static Connection connection;
    private static Optional<String> CONNECTION_HOST = Optional.empty();
    private static Optional<Integer> CONNECTION_PORT = Optional.empty();
    private static Optional<String> CONNECTION_USER = Optional.empty();
    private static Optional<String> CONNECTION_PASSWORD = Optional.empty();
    private static Optional<String> CONNECTION_DATABASE = Optional.empty();

    public static void setFromConfig(DatabaseConfig db) {
        CONNECTION_HOST = Optional.of(db.databaseHost());
        CONNECTION_PORT = Optional.of(db.databasePort());
        CONNECTION_USER = Optional.of(db.databaseUser());
        CONNECTION_PASSWORD = Optional.of(db.databasePassword());
        CONNECTION_DATABASE = Optional.of(db.databaseName());
    }

    private static boolean isDatabaseSet() {
        return CONNECTION_HOST.isPresent() &&
               CONNECTION_PORT.isPresent() &&
               CONNECTION_USER.isPresent() &&
               CONNECTION_PASSWORD.isPresent() &&
               CONNECTION_DATABASE.isPresent();
    }

    public static Optional<String> getDatabaseName() {
        return CONNECTION_DATABASE;
    }

    private static Connection generateConnection() throws SQLException {
        if (!isDatabaseSet()) {
            throw new IllegalStateException("Database connection parameters not set");
        }
        String CONNECTION_URL = "jdbc:mysql://" + CONNECTION_HOST.get() + ":" + CONNECTION_PORT.get() + "/" + CONNECTION_DATABASE.get();
        return DriverManager.getConnection(CONNECTION_URL, CONNECTION_USER.get(), CONNECTION_PASSWORD.get());
    }

    public static Connection connect() {
        try {
            if (connection != null && !connection.isClosed()) return connection;
            connection = DatabaseManager.generateConnection();
            connection.setAutoCommit(true);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
