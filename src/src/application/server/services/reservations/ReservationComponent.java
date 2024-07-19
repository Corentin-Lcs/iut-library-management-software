package application.server.services.reservations;

import application.server.entities.Subscriber;
import application.server.entities.Document;
import application.server.entities.types.ReservationDocumentNotAvailableException;
import application.server.managers.DataManager;
import application.server.managers.MailReminderManager;
import application.server.services.ServiceUtils;
import libraries.server.Component;
import libraries.server.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReservationComponent implements Component {
    private static final Logger LOGGER = LogManager.getLogger("Document reservation service");
    private static final String INVALID_INPUT_MESSAGE = "Entrée incorrecte, veuillez réessayer.";
    private static final String ASK_SUBSCRIBER_ID_MESSAGE = "Veuillez saisir votre numéro d'abonné :";
    private static final String ASK_DOCUMENT_ID_MESSAGE = "Veuillez saisir le numéro de l'ouvrage que vous souhaitez réserver :";

    @Override
    public void call(Service service) {
        try {
            StringBuilder askForSubscriberIdMessage = getWelcomeMessage();
            askForSubscriberIdMessage.append(ASK_SUBSCRIBER_ID_MESSAGE);
            service.send(askForSubscriberIdMessage.toString());
            int idSubscriber;
            while (true) {
                idSubscriber = ServiceUtils.askId(service);
                if (DataManager.getSubscriber(idSubscriber).isEmpty()) {
                    service.send(INVALID_INPUT_MESSAGE + System.lineSeparator() + ASK_SUBSCRIBER_ID_MESSAGE);
                } else {
                    break;
                }
            }
            Subscriber subscriber = DataManager.getSubscriber(idSubscriber).get();
            String askForDocumentIdMessage = "Bonjour " + subscriber.getName() + "," + System.lineSeparator() + ASK_DOCUMENT_ID_MESSAGE;
            service.send(askForDocumentIdMessage);
            int idDocument;
            while (true) {
                idDocument = ServiceUtils.askId(service);
                if (DataManager.getDocument(idDocument).isEmpty()) {
                    service.send(INVALID_INPUT_MESSAGE + System.lineSeparator() + ASK_DOCUMENT_ID_MESSAGE);
                } else {
                    break;
                }
            }
            Document document = DataManager.getDocument(idDocument).get();
            StringBuilder reservationResponse = new StringBuilder();
            try {
                document.reservationDoc(subscriber);
                reservationResponse.append("Réservation effectuée avec succès !").append(System.lineSeparator());
            } catch (ReservationDocumentNotAvailableException e) {
                reservationResponse.append(e.getMessage()).append(System.lineSeparator());
                boolean sendEmail = ServiceUtils.askBoolean(service, "Voulez-vous être notifié par email lorsque le document sera disponible ? (o/n)");
                if (sendEmail) {
                    MailReminderManager.addReminder(document, subscriber);
                }
                reservationResponse = new StringBuilder();
            } catch (Exception e) {
                reservationResponse.append(e.getMessage()).append(System.lineSeparator());
            } finally {
                reservationResponse.append("Souhaitez-vous réserver un autre document ? (o/n)");
                if (ServiceUtils.askBoolean(service, reservationResponse.toString())) {
                    call(service);
                } else {
                    String goodbyeMessage =
                        "Merci d'avoir choisi notre médiathèque." + System.lineSeparator() +
                        "À très bientôt." + System.lineSeparator() +
                        "[EXIT]";
                    service.send(goodbyeMessage);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error reserving document {}", e.getMessage());
            service.send("Une erreur est survenue, veuillez réessayer.");
        }
    }

    private StringBuilder getWelcomeMessage() {
        StringBuilder welcomeMessage = new StringBuilder();
        welcomeMessage.append("|--------------------[ SERVICE DE RÉSERVATION - MÉDIATHÈQUE DESFORGE ]--------------------|")
                      .append(System.lineSeparator().repeat(2))
                      .append("Bienvenue au service de réservation de la médiathèque.")
                      .append(System.lineSeparator())
                      .append("Voici le catalogue des ouvrages disponibles :")
                      .append(System.lineSeparator());
        DataManager.getDocuments().forEach(document -> welcomeMessage.append(document).append(System.lineSeparator()));
        return welcomeMessage;
    }
}
