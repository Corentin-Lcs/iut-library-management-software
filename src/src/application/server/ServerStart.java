package application.server;

import application.server.factories.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Corentin L. / Seweryn C.
 * @version 1.0
 */

public class ServerStart {
    private static final Logger LOGGER = LogManager.getLogger("Server Start");

    public static void main(String[] args) {
        try {
            Configurator.setRootLevel(Level.DEBUG);
            LOGGER.info("Starting the application");
            DatabaseFactory.setupDatabase();
            DataFactory.populateData();
            TimerFactory.setupTimers();
            EmailFactory.setupEmail();
            ServerFactory.setupPorts();
            ServerFactory.launchServer();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                LOGGER.info("Shutting down the application");
                try {
                    ServerFactory.stop();
                } catch (Exception e) {
                    LOGGER.error("Error while shutting down the server", e);
                }
            }, "Shutdown Thread"));
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
