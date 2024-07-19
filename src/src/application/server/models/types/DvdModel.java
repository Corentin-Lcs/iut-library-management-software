package application.server.models.types;

import application.server.entities.types.DvdEntity;
import application.server.models.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DvdModel extends Model<DvdEntity> {
    private static final Logger LOGGER = LogManager.getLogger("Dvd - DB Model");

    @Override
    public void save(DvdEntity entity) {
        LOGGER.warn("Attempted to save DVD, but this operation is not supported");
    }

    @Override
    public String getTableName() {
        return "Dvd";
    }

    @Override
    public DvdEntity getEntityInstance() {
        return new DvdEntity();
    }
}
