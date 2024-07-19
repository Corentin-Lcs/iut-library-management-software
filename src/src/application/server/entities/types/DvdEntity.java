package application.server.entities.types;

import application.server.entities.*;
import application.server.managers.DataManager;
import application.server.models.types.DocumentModel;
import application.server.models.types.DvdModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DvdEntity extends DocumentEntity {
    private static final String NOT_OLD_ENOUGH_MESSAGE = "Vous n'avez pas l'âge requis pour réserver ce document.";
    private static final int ADULT_AGE = 16;
    private final Object dvdLock = new Object();
    private SingleDocumentEntity document;
    private boolean adult;

    @Override
    public void reservationDoc(Subscriber sb) throws ReservationException {
        if (this.adult && sb.getAge() < ADULT_AGE) {
            throw new ReservationException(NOT_OLD_ENOUGH_MESSAGE);
        }
        document.reservationDoc(sb);
    }

    @Override
    public void borrowDoc(Subscriber sb) throws BorrowException {
        if (this.adult && sb.getAge() < ADULT_AGE) {
            throw new BorrowException(NOT_OLD_ENOUGH_MESSAGE);
        }
        document.borrowDoc(sb);
    }

    @Override
    public void returnDoc() throws ReturnException {
        this.document.returnDoc();
    }

    @Override
    public Entity<DocumentModel> mapEntity(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        this.setId(id);
        this.document = DataManager.getBaseDocument(id).orElse(null);
        this.adult = resultSet.getBoolean("isForAdult");
        return this;
    }

    @Override
    public void save() {
        synchronized (dvdLock) {
            new DvdModel().save(this);
        }
    }

    @Override
    public String toString() {
        return "DVD - " + this.document.toString() + " - " + (adult ? "PG16" : "Tout public");
    }
}
