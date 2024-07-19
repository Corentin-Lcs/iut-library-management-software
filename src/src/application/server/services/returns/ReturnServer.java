package application.server.services.returns;

import libraries.server.Server;

import java.io.IOException;
import java.util.Optional;

public class ReturnServer extends Server {
    private static final String SERVICE_NAME = "Return";
    private static final Class<ReturnService> SERVICE_CLASS = ReturnService.class;
    private static Optional<Integer> SERVICE_PORT = Optional.empty();

    public ReturnServer() throws IOException {
        super(
            SERVICE_CLASS,
            SERVICE_PORT.orElseThrow(() -> new IllegalStateException("The port for the Return service has not been set")),
            SERVICE_NAME
        );
    }

    public static void setServicePort(int port) {
        SERVICE_PORT = Optional.of(port);
    }
}
