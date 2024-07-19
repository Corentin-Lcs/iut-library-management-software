package application.server.entities.types;

import application.server.entities.Subscriber;
import application.server.entities.Entity;
import application.server.models.types.SubscriberModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;

public class SingleSubscriberEntity implements Subscriber, Entity<SubscriberModel> {
    private static final Logger LOGGER = LogManager.getLogger("Base Subscriber Entity");
    private final Object banLock = new Object();
    private int id;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String email;
    private boolean banned;
    private Optional<LocalDateTime> bannedUntil;

    public SingleSubscriberEntity() {
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Entity<SubscriberModel> mapEntity(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.firstName = resultSet.getString("firstName");
        this.lastName = resultSet.getString("lastName");
        this.birthdate = resultSet.getDate("birthdate").toLocalDate();
        this.email = resultSet.getString("email");
        this.banned = resultSet.getBoolean("isBanned");
        Timestamp bannedUntil = resultSet.getTimestamp("bannedUntil");
        if (bannedUntil == null) {
            this.bannedUntil = Optional.empty();
        } else {
            this.bannedUntil = Optional.of(bannedUntil.toLocalDateTime());
        }
        return this;
    }

    public void banUser() {
        setBanned(true);
        setBannedUntil(null);
        try {
            this.save();
        } catch (SQLException e) {
            LOGGER.error("Failed to ban user", e);
            throw new RuntimeException("Failed to ban user", e);
        }
    }

    public void unbanUser() {
        setBanned(false);
        setBannedUntil(null);
        try {
            this.save();
        } catch (SQLException e) {
            LOGGER.error("Failed to unban user", e);
            throw new RuntimeException("Failed to unban user", e);
        }
    }

    @Override
    public Optional<LocalDateTime> bannedUntil() {
        synchronized (banLock) {
            return bannedUntil;
        }
    }

    public void setBannedUntil(LocalDateTime bannedUntil) {
        synchronized (banLock) {
            this.bannedUntil = Optional.ofNullable(bannedUntil);
        }
    }

    @Override
    public void save() throws SQLException {
        synchronized (banLock) {
            new SubscriberModel().save(this);
        }
    }

    @Override
    public int getAge() {
        return Period.between(birthdate, LocalDate.now()).getYears();
    }

    @Override
    public String getName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean isBanned() {
        synchronized (banLock) {
            return banned;
        }
    }

    public void setBanned(boolean banned) {
        synchronized (banLock) {
            this.banned = banned;
        }
    }

    @Override
    public String getEmail() {
        return email;
    }
}
