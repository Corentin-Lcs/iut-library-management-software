package application.server.configs.components;

public record ServerConfigType(
    int port
) {
    public ServerConfigType {
        if (port < 0) {
            throw new IllegalArgumentException("The port must be greater than or equal to 0");
        }
    }
}
