package application.server.managers;

import application.server.entities.Subscriber;
import application.server.entities.Document;
import application.server.entities.types.DocumentLogEntity;
import application.server.entities.types.SingleDocumentEntity;

import java.util.List;
import java.util.Optional;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class DataManager {
    private static final ConcurrentHashMap<Integer, Document> documents = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, Subscriber> subscribers = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, SingleDocumentEntity> baseDocuments = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, DocumentLogEntity> documentLogs = new ConcurrentHashMap<>();

    public static void addDocument(Document document) {
        documents.put(document.number(), document);
    }

    public static void addSubscriber(Subscriber subscriber) {
        subscribers.put(subscriber.getId(), subscriber);
    }

    public static void addBaseDocument(SingleDocumentEntity document) {
        baseDocuments.put(document.getId(), document);
    }

    public static void addDocumentLog(DocumentLogEntity documentLog) {
        documentLogs.put(documentLog.getId(), documentLog);
    }

    public static Optional<Document> getDocument(int id) {
        return Optional.ofNullable(documents.get(id));
    }

    public static Optional<Subscriber> getSubscriber(int id) {
        return Optional.ofNullable(subscribers.get(id));
    }

    public static Optional<SingleDocumentEntity> getBaseDocument(int id) {
        return Optional.ofNullable(baseDocuments.get(id));
    }

    public static List<Document> getDocuments() {
        return new Vector<>(documents.values());
    }

    public static List<Subscriber> getSubscribers() {
        return new Vector<>(subscribers.values());
    }

    public static List<DocumentLogEntity> getDocumentLogs() {
        return new Vector<>(documentLogs.values());
    }
}
