package application.server.services.returns;

import application.server.entities.Document;
import application.server.managers.DataManager;
import application.server.services.ServiceUtils;
import libraries.server.Component;
import libraries.server.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReturnComponent implements Component {
    private static final Logger LOGGER = LogManager.getLogger("Return Document Component");
    private static final String INVALID_INPUT_MESSAGE = "Entrée incorrecte, veuillez réessayer.";
    private static final String ASK_DOCUMENT_ID_MESSAGE = "Veuillez saisir le numéro de l'ouvrage que vous souhaitez retourner :";

    @Override
    public void call(Service service) {
        try {
            StringBuilder askForDocumentIdMessage = getWelcomeMessage();
            askForDocumentIdMessage.append(ASK_DOCUMENT_ID_MESSAGE);
            service.send(askForDocumentIdMessage.toString());
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
            StringBuilder returnResponse = new StringBuilder();
            try {
                document.returnDoc();
                returnResponse.append("Retour effectué avec succès !").append(System.lineSeparator());
            } catch (Exception e) {
                returnResponse.append(e.getMessage()).append(System.lineSeparator());
            } finally {
                returnResponse.append("Souhaitez-vous retourner un autre document ? (o/n)");
                if (ServiceUtils.askBoolean(service, returnResponse.toString())) {
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
            LOGGER.error("Error while returning a document: {}", e.getMessage());
        }
    }

    private StringBuilder getWelcomeMessage() {
        StringBuilder welcomeMessage = new StringBuilder();
        welcomeMessage.append("|--------------------[ SERVICE DE RETOUR - MÉDIATHÈQUE DESFORGE ]--------------------|")
                      .append(System.lineSeparator().repeat(2))
                      .append("Bienvenue au service de retour de la médiathèque.")
                      .append(System.lineSeparator());
        return welcomeMessage;
    }
}
