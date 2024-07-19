package application.server.configs;

import application.server.configs.components.ServerConfigType;

public record ServerConfig(
    ServerConfigType documentReservationServer,
    ServerConfigType documentBorrowServer,
    ServerConfigType documentReturnServer
) {
    public ServerConfig {
        if (documentReservationServer == null) {
            throw new IllegalArgumentException("The document reservation configs must not be null");
        }
        if (documentBorrowServer == null) {
            throw new IllegalArgumentException("The document borrow configs must not be null");
        }
        if (documentReturnServer == null) {
            throw new IllegalArgumentException("The document return configs must not be null");
        }
    }

    public static String getServerConfigFilePath() {
        return "./config/server_config.json";
    }
}
