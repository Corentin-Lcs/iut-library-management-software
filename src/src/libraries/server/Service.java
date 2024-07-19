package libraries.server;

import libraries.server.communication.Protocol;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.SocketException;

public abstract class Service extends SocketProtocolLink {
    private final Class<? extends Component> component;

    public Service(Socket socket, Class<? extends Component> component, Class<? extends Protocol> protocol) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        super(socket, protocol);
        this.component = component;
    }

    public Service(Socket socket, Class<? extends Component> component) throws IOException {
        super(socket);
        this.component = component;
    }

    @Override
    public void run() {
        try {
            while (isRunning()) {
                this.protocol.setupCommunication();
                component.getConstructor().newInstance().call(this);
                while (isRunning()) {
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }
                }
            }
        } catch (SocketException ignored) {
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                this.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
