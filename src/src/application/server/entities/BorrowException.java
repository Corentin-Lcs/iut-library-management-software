package application.server.entities;

public class BorrowException extends RuntimeException {
    public BorrowException(String message) {
        super("EMPRUNT IMPOSSIBLE : " + message);
    }
}
