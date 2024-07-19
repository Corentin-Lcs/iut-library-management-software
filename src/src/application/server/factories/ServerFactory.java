package application.server.factories;

import application.server.configs.ServerConfig;
import application.server.managers.ConfigurationManager;
import application.server.managers.ServerManager;
import application.server.services.borrows.BorrowServer;
import application.server.services.reservations.ReservationServer;
import application.server.services.returns.ReturnServer;
import libraries.server.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ServerFactory {
    private static final Logger LOGGER = LogManager.getLogger("Server Factory");
    private static final Vector<Server> launchedServers = new Vector<>();

    public static void setupPorts() {
        LOGGER.info("Setting up server ports (reading from configs)");
        ServerConfig serverConfig = ConfigurationManager.getConfig(ServerConfig.class, ServerConfig.getServerConfigFilePath());
        BorrowServer.setServicePort(serverConfig.documentBorrowServer().port());
        ReservationServer.setServicePort(serverConfig.documentReservationServer().port());
        ReturnServer.setServicePort(serverConfig.documentReturnServer().port());
        LOGGER.info("Server ports set");
    }

    private static List<Class<? extends Server>> getServerList() {
        List<Class<? extends Server>> serverList = new ArrayList<>();
        serverList.add(BorrowServer.class);
        serverList.add(ReservationServer.class);
        serverList.add(ReturnServer.class);
        return serverList;
    }

    public static void launchServer() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        LOGGER.info("Launching servers");
        for (Class<? extends Server> serverClass : getServerList()) {
            Server server = ServerManager.start(serverClass);
            launchedServers.add(server);
        }
        LOGGER.info("Servers launched");
    }

    public static void stop() {
        for (Server server : launchedServers) {
            ServerManager.stop(server);
        }
    }
}
