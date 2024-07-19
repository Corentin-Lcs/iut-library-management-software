package application.server.services.reservations;

import libraries.server.Server;

import java.io.IOException;
import java.util.Optional;

public class ReservationServer extends Server {
    private static final String SERVICE_NAME = "Reservation";
    private static final Class<ReservationService> SERVICE_CLASS = ReservationService.class;
    private static Optional<Integer> SERVICE_PORT = Optional.empty();

    public ReservationServer() throws IOException {
        super(
            SERVICE_CLASS,
            SERVICE_PORT.orElseThrow(() -> new IllegalStateException("The port for the Reservation service has not been set")),
            SERVICE_NAME
        );
    }

    public static void setServicePort(int port) {
        SERVICE_PORT = Optional.of(port);
    }
}
