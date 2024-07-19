package application.server.entities;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

public interface Subscriber {
    int getId();

    int getAge();

    String getName();

    boolean isBanned();

    void banUser();

    void unbanUser();

    void setBannedUntil(LocalDateTime bannedUntil);

    Optional<LocalDateTime> bannedUntil();

    String getEmail();

    void save() throws SQLException;
}
