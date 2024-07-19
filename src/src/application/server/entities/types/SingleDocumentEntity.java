package application.server.entities.types;

import application.server.entities.Subscriber;
import application.server.entities.BorrowException;
import application.server.entities.ReservationException;
import application.server.entities.ReturnException;
import application.server.managers.MailReminderManager;
import application.server.managers.TimerManager;
import application.server.models.types.DocumentModel;
import application.server.timers.tasks.UnbanUserTask;
import application.server.timers.tasks.BorrowTask;
import application.server.timers.tasks.ReservationTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

public final class SingleDocumentEntity extends DocumentEntity {
    private static final Logger LOGGER = LogManager.getLogger("Base Document Entity");
    private final Object logLock = new Object();
    private final Object stateLock = new Object();
    private DocumentState state;
    private Optional<DocumentLogEntity> lastLog;

    private static final String ALREADY_BORROWED_MESSAGE = "Ce document est déjà emprunté par un autre abonné.";
    private static final String ALREADY_RESERVED_MESSAGE = "Ce document est déjà réservé par un autre abonné.";
    private static final String BORROWED_SELF_MESSAGE = "Vous avez déjà emprunté ce document.";
    private static final String RESERVED_SELF_MESSAGE = "Vous avez déjà réservé ce document.";

    private static void logOptionalError(DocumentState state, String notFoundElement) {
        LOGGER.error("{} not found in {} log - (THIS SHOULD NOT HAPPEN)", notFoundElement, state.getName());
    }

    public DocumentState getState() {
        synchronized (stateLock) {
            return state;
        }
    }

    public void setLastLog(DocumentLogEntity lastLog) {
        this.lastLog = Optional.of(lastLog);
    }

    @Override
    public void reservationDoc(Subscriber sb) throws ReservationException {
        LOGGER.debug("Trying to reserve document {} with state {} by subscriber {}", this.number(), getState(), sb.getId());
        checkForBan(sb);
        synchronized (stateLock) {
            switch (getState()) {
                case BORROWED -> {
                    Subscriber subscriber = getSubscriberFromLastLog(ALREADY_BORROWED_MESSAGE);
                    if (!subscriber.equals(sb)) {
                        throw new ReservationDocumentNotAvailableException(ALREADY_BORROWED_MESSAGE);
                    } else {
                        throw new ReservationException(BORROWED_SELF_MESSAGE);
                    }
                }
                case RESERVED -> {
                    Subscriber subscriber = getSubscriberFromLastLog(ALREADY_RESERVED_MESSAGE);
                    if (!subscriber.equals(sb)) {
                        throw new ReservationDocumentNotAvailableException(ALREADY_RESERVED_MESSAGE);
                    } else {
                        throw new ReservationException(RESERVED_SELF_MESSAGE);
                    }
                }
                case FREE -> {
                    ReservationTask task = new ReservationTask(sb, this);
                    TimerManager.startTimer(task.getTaskIdentifier(), task);
                    processLog(sb, DocumentState.RESERVED);
                }
            }
        }
    }

    @Override
    public void borrowDoc(Subscriber sb) throws BorrowException {
        LOGGER.debug("Trying to borrow document {} with state {} by subscriber {}", this.number(), getState(), sb.getId());
        checkForBan(sb);
        switch (getState()) {
            case BORROWED -> {
                Subscriber subscriber = getSubscriberFromLastLog(ALREADY_BORROWED_MESSAGE);
                if (!subscriber.equals(sb)) {
                    throw new BorrowException(ALREADY_BORROWED_MESSAGE);
                } else {
                    throw new BorrowException(BORROWED_SELF_MESSAGE);
                }
            }
            case RESERVED -> {
                synchronized (stateLock) {
                    Subscriber subscriber = getSubscriberFromLastLog(ALREADY_RESERVED_MESSAGE);
                    if (!subscriber.equals(sb)) {
                        throw new BorrowException(ALREADY_RESERVED_MESSAGE);
                    }
                    ReservationTask task = new ReservationTask(sb, this);
                    TimerManager.stopTimer(task.getTaskIdentifier());
                    processBorrow(sb);
                }
            }
            case FREE -> processBorrow(sb);
        }
    }

    public void processBorrow(Subscriber sb) throws BorrowException {
        BorrowTask task = new BorrowTask(sb);
        TimerManager.startTimer(task.getTaskIdentifier(), task);
        processLog(sb, DocumentState.BORROWED);
    }

    @Override
    public void returnDoc() throws ReturnException {
        synchronized (stateLock) {
            DocumentState currentState = getState();
            LOGGER.debug("Trying to return document {} with state {}", this.number(), currentState);
            switch (currentState) {
                case FREE -> throw new ReturnException("Ce document n'est pas emprunté.");
                case RESERVED -> throw new ReturnException("Ce document est actuellement réservé.");
                case BORROWED -> {
                    Subscriber sb = getSubscriberFromLastLog("Erreur Serveur");
                    processLog(null, DocumentState.FREE);
                    MailReminderManager.sendReminder(this);
                    try {
                        BorrowTask task = new BorrowTask(sb);
                        TimerManager.stopTimer(task.getTaskIdentifier());
                    } catch (IllegalArgumentException e) {
                        LOGGER.debug("Banned user returned document, starting ban timer");
                        long duration = UnbanUserTask.getDefaultDurationInSeconds();
                        UnbanUserTask banUserTask = new UnbanUserTask(sb, duration);
                        sb.setBannedUntil(LocalDateTime.now().plusSeconds(duration));
                        try {
                            sb.save();
                        } catch (SQLException ex) {
                            LOGGER.error("Failed to save subscriber", ex);
                            throw new RuntimeException("Failed to save subscriber", ex);
                        }
                        TimerManager.startTimer(banUserTask.getTaskIdentifier(), banUserTask);
                        String bannedUntil = sb.bannedUntil().isPresent() ? sb.bannedUntil().get().toString() : "indéfini";
                        throw new ReturnException("Vous avez été banni jusqu'au " + bannedUntil + ".");
                    }
                }
            }
        }
    }

    public void processLog(Subscriber sb, DocumentState newState) {
        try {
            synchronized (stateLock) {
                this.state = newState;
                this.save();
                synchronized (logLock) {
                    if (this.lastLog.isEmpty()) {
                        this.lastLog = Optional.of(new DocumentLogEntity());
                    }
                    DocumentLogEntity log = this.lastLog.orElseGet(DocumentLogEntity::new);
                    log.setNewLog(sb, this);
                    log.save();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while saving document log :", e);
        }
    }

    @Override
    public SingleDocumentEntity mapEntity(ResultSet resultSet) throws SQLException {
        this.setId(resultSet.getInt("id"));
        this.setTitle(resultSet.getString("title"));
        this.state = DocumentState.fromInt(resultSet.getInt("idState")).orElseThrow();
        this.lastLog = Optional.empty();
        return this;
    }

    @Override
    public void save() throws SQLException {
        synchronized (stateLock) {
            new DocumentModel().save(this);
        }
    }

    public void cancelReservation(Subscriber subscriber) {
        this.processLog(subscriber, DocumentState.FREE);
        MailReminderManager.sendReminder(this);
    }

    @Override
    public String toString() {
        return this.number() + " - " + this.getTitle() + " (" + this.state.getName() + ")";
    }

    private Subscriber getSubscriberFromLastLog(String defaultLogErrorMessage) {
        synchronized (logLock) {
            return this.lastLog
                    .orElseThrow(() -> {
                        logOptionalError(state, "Last log");
                        return new RuntimeException(defaultLogErrorMessage);
                    })
                    .getSubscriber()
                    .orElseThrow(() -> {
                        logOptionalError(state, "Subscriber");
                        return new RuntimeException(defaultLogErrorMessage);
                    });
        }
    }

    private void checkForBan(Subscriber sb) throws ReservationException {
        if (sb.isBanned()) {
            String bannedUntil = sb.bannedUntil().isPresent() ? sb.bannedUntil().get().toString() : "indéfini";
            throw new ReservationException("Vous avez été banni jusqu'au " + bannedUntil + ".");
        }
    }
}
