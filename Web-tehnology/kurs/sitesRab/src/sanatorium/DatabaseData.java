package sanatorium;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DatabaseData implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<User> users = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();
    private List<Payment> payments = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();
    private List<SupportRequest> supportRequests = new ArrayList<>();

    private int nextUserId = 1;
    private int nextBookingId = 1;
    private int nextPaymentId = 1;
    private int nextReviewId = 1;
    private int nextSupportId = 1;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<SupportRequest> getSupportRequests() {
        return supportRequests;
    }

    public void setSupportRequests(List<SupportRequest> supportRequests) {
        this.supportRequests = supportRequests;
    }

    public int getNextUserId() {
        return nextUserId;
    }

    public void setNextUserId(int nextUserId) {
        this.nextUserId = nextUserId;
    }

    public int getNextBookingId() {
        return nextBookingId;
    }

    public void setNextBookingId(int nextBookingId) {
        this.nextBookingId = nextBookingId;
    }

    public int getNextPaymentId() {
        return nextPaymentId;
    }

    public void setNextPaymentId(int nextPaymentId) {
        this.nextPaymentId = nextPaymentId;
    }

    public int getNextReviewId() {
        return nextReviewId;
    }

    public void setNextReviewId(int nextReviewId) {
        this.nextReviewId = nextReviewId;
    }

    public int getNextSupportId() {
        return nextSupportId;
    }

    public void setNextSupportId(int nextSupportId) {
        this.nextSupportId = nextSupportId;
    }
}
