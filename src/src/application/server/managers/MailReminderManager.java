package application.server.managers;

import application.server.entities.Subscriber;
import application.server.entities.Document;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class MailReminderManager {
    private static final Logger LOGGER = LogManager.getLogger("Mail Reminder Manager");
    private static final ConcurrentHashMap<Integer, List<Integer>> reminders = new ConcurrentHashMap<>();

    public static void addReminder(Document document, Subscriber subscriber) {
        LOGGER.debug("Adding reminder for document {} and subscriber {}", document.number(), subscriber.getId());
        if (!reminders.containsKey(document.number())) {
            reminders.put(document.number(), new Vector<>());
        }
        reminders.get(document.number()).add(subscriber.getId());
        LOGGER.debug("Reminder added for document {} and subscriber {}", document.number(), subscriber.getId());
    }

    public static void sendReminder(Document document) {
        LOGGER.info("Sending reminder for document {}", document.number());
        List<Integer> subscribers = reminders.get(document.number());
        if (subscribers == null) {
            LOGGER.warn("No reminder found for document {}", document.number());
            return;
        }
        for (Integer subscriber : subscribers) {
            Document documentEntity = DataManager.getDocument(document.number()).orElseThrow();
            Subscriber subscriberEntity = DataManager.getSubscriber(subscriber).orElseThrow();
            LOGGER.debug("Sending reminder to subscriber {}", subscriber);
            try {
                EmailManager.sendEmail(subscriberEntity.getEmail(), "Un document est de nouveau disponible !",
                        String.format(
                                """
                                Bonjour %s,
                                \s
                                Nous avons le plaisir de vous informer que le document que vous avez demandé, et qui n'était pas disponible auparavant, est à nouveau disponible à la Médiathèque DesForge.
                                \s
                                Vous êtes invité à venir le récupérer à votre convenance aux horaires d'ouverture de la médiathèque.
                                \s
                                Pour toute assistance supplémentaire, n'hésitez pas à nous contacter.
                                \s
                                Cordialement,
                                Médiathèque DesForge
                                """,
                            subscriberEntity.getName(),
                            documentEntity
                        )
                );
            } catch (Exception e) {
                LOGGER.error("Error sending reminder to subscriber {}", subscriber, e);
            }
        }
        clearReminders(document);
    }

    private static void clearReminders(Document document) {
        LOGGER.debug("Clearing reminders for document {}", document.number());
        reminders.remove(document.number());
    }
}
