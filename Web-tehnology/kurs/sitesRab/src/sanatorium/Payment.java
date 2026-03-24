package sanatorium;

import java.io.Serializable;

public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int userId;
    private int bookingId;
    private String createdAt;
    private String title;
    private int amount;
    private String status;
    private String paidAt;

    public Payment() {
    }

    public Payment(int id, int userId, int bookingId, String createdAt, String title, int amount, String status, String paidAt) {
        this.id = id;
        this.userId = userId;
        this.bookingId = bookingId;
        this.createdAt = createdAt;
        this.title = title;
        this.amount = amount;
        this.status = status;
        this.paidAt = paidAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getCreatedAt() {
        return createdAt == null ? "" : createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaidAt() {
        return paidAt == null ? "" : paidAt;
    }

    public void setPaidAt(String paidAt) {
        this.paidAt = paidAt;
    }
}
