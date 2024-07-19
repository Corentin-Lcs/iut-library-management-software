package libraries.server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private final int serverPort;
    private final String serverName;
    private final ServerSocket listenSocket;
    private final Class<? extends Service> serviceClass;
    private Service currentServiceClass;
    private volatile boolean isRunning = false;
    private Thread serverThread;

    public Server(Class<? extends Service> serviceClass, int serverPort, String serverName) throws IOException {
        this.serverPort = serverPort;
        this.serverName = serverName;
        this.listenSocket = new ServerSocket(serverPort);
        this.serviceClass = serviceClass;
        this.currentServiceClass = null;
    }

    public Server(Class<? extends Service> serviceClass, int serverPort) throws IOException {
        this(serviceClass, serverPort, "Not defined");
    }

    public String getName() {
        return this.serverName;
    }

    public String getIp() {
        return "127.0.0.1:" + this.serverPort;
    }

    public void run() {
        isRunning = true;
        try {
            while (isRunning) {
                try {
                    Socket clientSocket = this.listenSocket.accept();
                    currentServiceClass = this.serviceClass.getConstructor(Socket.class).newInstance(clientSocket);
                    currentServiceClass.start();
                } catch (IOException e) {
                    if (!isRunning) {
                        break;
                    }
                    e.printStackTrace();
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            try {
                this.listenSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        serverThread = new Thread(this, this.serverName);
        serverThread.start();
    }

    public void stop() {
        isRunning = false;
        try {
            listenSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (serverThread != null) {
            serverThread.interrupt();
        }
    }
}
