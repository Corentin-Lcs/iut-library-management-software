package libraries.server.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class Protocol {
    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Protocol(Socket socket) throws IOException {
        this.socket = socket;
        setupCommunication();
    }

    public void setupCommunication() throws IOException {
        try {
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Error setting up communication: " + e.getMessage());
            throw e;
        }
    }

    public void send(String data) {
        if (out != null) {
            out.println(this.encode(data));
        } else {
            System.err.println("Output stream is not initialized");
        }
    }

    public String read() throws IOException {
        if (in != null) {
            return this.decode(in.readLine());
        } else {
            throw new IOException("Input stream is not initialized");
        }
    }

    public void close() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing communication: " + e.getMessage());
        }
    }

    abstract public String encode(String data);

    abstract public String decode(String data);
}
