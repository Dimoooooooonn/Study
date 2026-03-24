package sanatorium;

import java.io.Serializable;

public class Review implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private int rating;
    private String text;
    private String createdAt;

    public Review() {
    }

    public Review(int id, String name, int rating, String text, String createdAt) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.text = text;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public int getRating() {
        return rating;
    }

    public String getText() {
        return text == null ? "" : text;
    }

    public String getCreatedAt() {
        return createdAt == null ? "" : createdAt;
    }
}
