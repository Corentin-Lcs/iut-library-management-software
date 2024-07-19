package application.server.entities.types;

import application.server.entities.ReservationException;

public class ReservationDocumentNotAvailableException extends ReservationException {
    public ReservationDocumentNotAvailableException(String message) {
        super(message);
    }
}
