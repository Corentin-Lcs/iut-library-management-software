package application.clientuser;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ClientFactory {
    private static final int RESERVATION_PORT = 3000;
    private static final String RESERVATION_CLIENT = "localhost";

    public static ClientManager create() throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException, NoSuchMethodException {
        ClientManager clientManager = new ClientManager(RESERVATION_CLIENT, RESERVATION_PORT);
        System.out.println(clientManager.read());
        return clientManager;
    }

    public static void manage(ClientManager clientManager) throws IOException {
        boolean sendMessage = false;
        while (true) {
            clientManager.send(sendMessage ? "" : clientManager.getInputStream().readLine());
            sendMessage = false;
            String response = clientManager.read();
            if (response.endsWith("[EXIT]")) {
                System.out.println(response.replace("[EXIT]", "").trim());
                break;
            }
            if (response.endsWith("[SEND]")) {
                response = response.replace("[SEND]", "").trim();
                sendMessage = true;
            }
            System.out.println(response);
        }
    }

    public static void close(ClientManager clientManager) throws IOException {
        clientManager.close();
    }
}
