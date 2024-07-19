package libraries.server.communication;

import java.io.IOException;
import java.net.Socket;

public class StreamIOProtocol extends Protocol {
    private final String LINE_BREAK = "##";

    public StreamIOProtocol(Socket socket) throws IOException {
        super(socket);
    }

    public String encode(String data) {
        if (data != null && !data.isEmpty()) {
            return data.replaceAll(System.lineSeparator(), LINE_BREAK);
        }
        return "";
    }

    public String decode(String data) {
        if (data != null && !data.isEmpty()) {
            return data.replaceAll(LINE_BREAK, System.lineSeparator());
        }
        return null;
    }
}
