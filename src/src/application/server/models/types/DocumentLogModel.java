package application.server.models.types;

import application.server.entities.types.DocumentLogEntity;
import application.server.models.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DocumentLogModel extends Model<DocumentLogEntity> {
    private static final Logger LOGGER = LogManager.getLogger("DocumentLog - DB Model");

    @Override
    public void save(DocumentLogEntity entity) throws SQLException {
        if (exists(entity)) {
            runUpdate(entity);
        } else {
            runInsert(entity);
        }
    }

    private void runInsert(DocumentLogEntity entity) throws SQLException {
        LOGGER.debug("Inserting new document log {}", entity.getId());
        PreparedStatement preparedStatement = super.prepareStatement("INSERT INTO " + getFullTableName() + " (id, idSubscriber, time) VALUES (?, ?, ?)");
        preparedStatement.setInt(1, entity.getDocument().number());
        if (entity.getSubscriber().isPresent()) {
            preparedStatement.setInt(2, entity.getSubscriber().get().getId());
        } else {
            preparedStatement.setNull(2, java.sql.Types.INTEGER);
        }
        preparedStatement.setTimestamp(3, Timestamp.valueOf(entity.getTime()));
        preparedStatement.executeUpdate();
    }

    private void runUpdate(DocumentLogEntity entity) throws SQLException {
        LOGGER.debug("Updating document log {}", entity.getId());
        PreparedStatement preparedStatement = super.prepareStatement("UPDATE " + getFullTableName() + " SET idSubscriber = ?, time = ? WHERE id = ?");
        if (entity.getSubscriber().isPresent()) {
            preparedStatement.setInt(1, entity.getSubscriber().get().getId());
        } else {
            preparedStatement.setNull(1, java.sql.Types.INTEGER);
        }
        preparedStatement.setTimestamp(2, Timestamp.valueOf(entity.getTime()));
        preparedStatement.setInt(3, entity.getId());
        preparedStatement.executeUpdate();
    }

    private boolean exists(DocumentLogEntity entity) throws SQLException {
        PreparedStatement preparedStatement = super.prepareStatement("SELECT * FROM " + getFullTableName() + " WHERE id = ?");
        preparedStatement.setInt(1, entity.getId());
        try {
            return preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            LOGGER.error("Error while checking if document log exists", e);
            throw new SQLException("Error while checking if document log exists", e);
        }
    }

    @Override
    public String getTableName() {
        return "DocumentChangeLog";
    }

    @Override
    public DocumentLogEntity getEntityInstance() {
        return new DocumentLogEntity();
    }
}
