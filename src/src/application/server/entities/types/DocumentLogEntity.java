package application.server.entities.types;

import application.server.entities.Subscriber;
import application.server.entities.Entity;
import application.server.managers.DataManager;
import application.server.models.types.DocumentLogModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

public final class DocumentLogEntity implements Entity<DocumentLogModel> {
    private final Object logLock = new Object();
    private Subscriber subscriber;
    private SingleDocumentEntity document;
    private LocalDateTime time;

    public DocumentLogEntity() {
    }

    public void setNewLog(Subscriber subscriber, SingleDocumentEntity document) {
        synchronized (logLock) {
            this.subscriber = subscriber;
            this.document = document;
            this.time = LocalDateTime.now();
        }
    }

    @Override
    public int getId() {
        return document.number();
    }

    @Override
    public DocumentLogEntity mapEntity(ResultSet resultSet) throws SQLException {
        this.document = DataManager.getBaseDocument(resultSet.getInt("id")).orElseThrow();
        this.subscriber = DataManager.getSubscriber(resultSet.getInt("idSubscriber")).orElse(null);
        this.time = resultSet.getTimestamp("time").toLocalDateTime();
        this.document.setLastLog(this);
        return this;
    }

    @Override
    public void save() throws SQLException {
        synchronized (logLock) {
            new DocumentLogModel().save(this);
        }
    }

    public LocalDateTime getTime() {
        synchronized (logLock) {
            return time;
        }
    }

    public Optional<Subscriber> getSubscriber() {
        synchronized (logLock) {
            return Optional.ofNullable(subscriber);
        }
    }

    public SingleDocumentEntity getDocument() {
        synchronized (logLock) {
            return document;
        }
    }
}
