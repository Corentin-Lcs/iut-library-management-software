package application.server.services.borrows;

import libraries.server.Service;

import java.io.IOException;
import java.net.Socket;

public class BorrowService extends Service {
    public BorrowService(Socket socket) throws IOException {
        super(socket, BorrowComponent.class);
    }
}
