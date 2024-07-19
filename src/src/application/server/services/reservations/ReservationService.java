package application.server.services.reservations;

import libraries.server.Service;

import java.io.IOException;
import java.net.Socket;

public class ReservationService extends Service {
    public ReservationService(Socket socket) throws IOException {
        super(socket, ReservationComponent.class);
    }
}
