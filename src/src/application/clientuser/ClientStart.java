package application.clientuser;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Corentin L. / Seweryn C.
 * @version 1.0
 */

public class ClientStart {
    public static void main(String[] args) {
        try {
            ClientManager clientManager = ClientFactory.create();
            ClientFactory.manage(clientManager);
            ClientFactory.close(clientManager);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | IOException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
