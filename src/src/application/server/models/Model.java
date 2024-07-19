package application.server.models;

import application.server.entities.Entity;
import application.server.managers.DatabaseManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public abstract class Model<T extends Entity<?>> {
    private static final Logger LOGGER = LogManager.getLogger("DB Model");
    private final Connection connection;

    public Model() {
        this.connection = DatabaseManager.connect();
    }

    public abstract void save(T entity) throws SQLException;

    public String getFullTableName() {
        return getDatabaseName() + "." + getTableName();
    }

    public abstract String getTableName();

    public String getDatabaseName() {
        return DatabaseManager.getDatabaseName().orElseThrow(() -> new IllegalStateException("Database name not set"));
    }

    public abstract T getEntityInstance();

    public Vector<T> get() throws SQLException {
        LOGGER.info("Fetching entries from database for {}", this.getClass().getSimpleName());
        try {
            ResultSet resultSet = entries();
            Vector<T> entities = new Vector<>();
            while (resultSet.next()) {
                entities.add(mapEntity(resultSet));
            }
            return entities;
        } catch (SQLException e) {
            throw new SQLException("Error while fetching entries from database", e);
        }
    }

    private T mapEntity(ResultSet resultSet) throws SQLException {
        T entity = getEntityInstance();
        return (T) entity.mapEntity(resultSet);
    }

    private ResultSet entries() throws SQLException {
        try {
            return connection
                   .createStatement()
                   .executeQuery("SELECT * FROM " + getFullTableName());
        } catch (SQLException e) {
            throw new SQLException("Error while fetching entries from database", e);
        }
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }
}
