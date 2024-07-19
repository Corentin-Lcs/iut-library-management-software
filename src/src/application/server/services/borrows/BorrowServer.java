package application.server.services.borrows;

import libraries.server.Server;

import java.io.IOException;
import java.util.Optional;

public class BorrowServer extends Server {
    private static final String SERVICE_NAME = "Borrow";
    private static final Class<BorrowService> SERVICE_CLASS = BorrowService.class;
    private static Optional<Integer> SERVICE_PORT = Optional.empty();

    public BorrowServer() throws IOException {
        super(
            SERVICE_CLASS,
            SERVICE_PORT.orElseThrow(() -> new IllegalStateException("The port for the Borrow service has not been set")),
            SERVICE_NAME
        );
    }

    public static void setServicePort(int port) {
        SERVICE_PORT = Optional.of(port);
    }
}
