package application.server.models.types;

import application.server.entities.types.DocumentState;
import application.server.entities.types.SingleDocumentEntity;
import application.server.models.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DocumentModel extends Model<SingleDocumentEntity> {
    private static final Logger LOGGER = LogManager.getLogger("Document - DB Model");

    @Override
    public void save(SingleDocumentEntity entity) throws SQLException {
        int id = entity.getId();
        DocumentState state = entity.getState();
        int newState = state.getStateId();
        LOGGER.debug("Updating document {} : new state {}", id, state.getName());
        PreparedStatement preparedStatement = super.prepareStatement("UPDATE " + getFullTableName() + " SET idState = ? WHERE id = ?");
        preparedStatement.setInt(1, newState);
        preparedStatement.setInt(2, id);
        preparedStatement.executeUpdate();
    }

    @Override
    public String getTableName() {
        return "Document";
    }

    @Override
    public SingleDocumentEntity getEntityInstance() {
        return new SingleDocumentEntity();
    }
}
