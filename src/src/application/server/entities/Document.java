package application.server.entities;

public interface Document {
    int number();

    void reservationDoc(Subscriber sb) throws ReservationException;

    void borrowDoc(Subscriber sb) throws BorrowException;

    void returnDoc() throws ReturnException;
}
