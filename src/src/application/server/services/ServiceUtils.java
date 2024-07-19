package application.server.services;

import libraries.server.Service;

import java.io.IOException;

public class ServiceUtils {
    private static final String INVALID_INPUT_MESSAGE = "Entrée incorrecte, veuillez réessayer.";
    private static final String ASK_MESSAGE = "Entrez votre valeur : ";

    public static String askToRetry(Service service, String message) throws IOException {
        service.send(INVALID_INPUT_MESSAGE + System.lineSeparator() + message);
        return service.read();
    }

    public static int askId(Service service) throws IOException {
        return ServiceUtils.askId(service, ASK_MESSAGE);
    }

    public static int askId(Service service, String askMessage) throws IOException {
        String response = service.read();
        while (response == null || !response.matches("\\d+")) {
            response = ServiceUtils.askToRetry(service, askMessage);
        }
        return Integer.parseInt(response);
    }

    public static boolean askBoolean(Service service, String askMessage) throws IOException {
        service.send(askMessage);
        String response = service.read();
        while (!("o".equals(response) || "n".equals(response))) {
            response = ServiceUtils.askToRetry(service, askMessage);
        }
        return "o".equals(response);
    }
}
