package application.server.entities.types;

import application.server.entities.Document;
import application.server.entities.Entity;
import application.server.models.types.DocumentModel;

public abstract class DocumentEntity implements Entity<DocumentModel>, Document {
    private int id;
    private String title;

    public int number() {
        return id;
    }

    public int getId() {
        return number();
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
