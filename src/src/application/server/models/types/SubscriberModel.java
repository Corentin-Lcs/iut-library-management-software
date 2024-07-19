package application.server.models.types;

import application.server.entities.types.SingleSubscriberEntity;
import application.server.models.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

public class SubscriberModel extends Model<SingleSubscriberEntity> {
    private static final Logger LOGGER = LogManager.getLogger("Subscriber - DB Model");

    public SubscriberModel() {
        super();
    }

    @Override
    public void save(SingleSubscriberEntity entity) throws SQLException {
        int id = entity.getId();
        boolean banned = entity.isBanned();
        Optional<LocalDateTime> bannedUntil = entity.bannedUntil();
        LOGGER.debug("Updating subscriber {} : {}", id, banned ? "banned" : "unbanned");
        PreparedStatement preparedStatement = super.prepareStatement("UPDATE " + getFullTableName() + " SET isBanned = ?, bannedUntil = ? WHERE id = ?");
        preparedStatement.setBoolean(1, banned);
        if (bannedUntil.isPresent()) {
            preparedStatement.setTimestamp(2, Timestamp.valueOf(bannedUntil.get()));
        } else {
            preparedStatement.setNull(2, java.sql.Types.TIMESTAMP);
        }
        preparedStatement.setInt(3, id);
        preparedStatement.executeUpdate();
    }

    @Override
    public String getTableName() {
        return "Subscriber";
    }

    @Override
    public SingleSubscriberEntity getEntityInstance() {
        return new SingleSubscriberEntity();
    }
}
