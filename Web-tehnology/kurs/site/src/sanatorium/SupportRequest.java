package sanatorium;

import java.io.Serializable;

public class SupportRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int userId;
    private String createdAt;
    private String subject;
    private String category;
    private String message;
    private String status;
    private String answer;

    public SupportRequest() {
    }

    public SupportRequest(int id, int userId, String createdAt, String subject, String category,
                          String message, String status, String answer) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.subject = subject;
        this.category = category;
        this.message = message;
        this.status = status;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getCreatedAt() {
        return createdAt == null ? "" : createdAt;
    }

    public String getSubject() {
        return subject == null ? "" : subject;
    }

    public String getCategory() {
        return category == null ? "" : category;
    }

    public String getMessage() {
        return message == null ? "" : message;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public String getAnswer() {
        return answer == null ? "" : answer;
    }
}
