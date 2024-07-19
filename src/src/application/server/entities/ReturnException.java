package application.server.entities;

public class ReturnException extends RuntimeException {
    public ReturnException(String message) {
        super("PROBLÈME SURVENU PENDANT LE RETOUR : " + message);
    }
}
