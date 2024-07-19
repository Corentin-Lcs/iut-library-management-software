package application.clientlibrary;

import libraries.server.SocketProtocolLink;
import libraries.server.communication.StreamIOProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class ClientManager extends SocketProtocolLink {
    public ClientManager(String host, int port) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException, NoSuchMethodException {
        super(new Socket(host, port), StreamIOProtocol.class);
    }

    public BufferedReader getInputStream() {
        return new BufferedReader(new InputStreamReader(System.in));
    }
}
