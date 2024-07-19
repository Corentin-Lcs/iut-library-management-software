package application.clientlibrary;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ClientFactory {
    private static final String DEFAULT_CLIENT_HOST = "localhost";

    public static ClientManager create(String[] args) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException, NoSuchMethodException {
        ClientManager clientManager = new ClientManager(DEFAULT_CLIENT_HOST, Integer.parseInt(args[0]));
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
