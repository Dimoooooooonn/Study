package sanatorium;

import java.io.Serializable;

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int userId;
    private String checkIn;
    private String checkOut;
    private String guests;
    private String purpose;
    private String room;
    private String fullName;
    private String phone;
    private String email;
    private String comment;
    private String options;
    private String status;
    private String createdAt;
    private int totalPrice;

    public Booking() {
    }

    public Booking(int id, int userId, String checkIn, String checkOut, String guests, String purpose,
                   String room, String fullName, String phone, String email, String comment,
                   String options, String status, String createdAt, int totalPrice) {
        this.id = id;
        this.userId = userId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.guests = guests;
        this.purpose = purpose;
        this.room = room;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.comment = comment;
        this.options = options;
        this.status = status;
        this.createdAt = createdAt;
        this.totalPrice = totalPrice;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getCheckIn() {
        return checkIn == null ? "" : checkIn;
    }

    public String getCheckOut() {
        return checkOut == null ? "" : checkOut;
    }

    public String getGuests() {
        return guests == null ? "" : guests;
    }

    public String getPurpose() {
        return purpose == null ? "" : purpose;
    }

    public String getRoom() {
        return room == null ? "" : room;
    }

    public String getFullName() {
        return fullName == null ? "" : fullName;
    }

    public String getPhone() {
        return phone == null ? "" : phone;
    }

    public String getEmail() {
        return email == null ? "" : email;
    }

    public String getComment() {
        return comment == null ? "" : comment;
    }

    public String getOptions() {
        return options == null ? "" : options;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public String getCreatedAt() {
        return createdAt == null ? "" : createdAt;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public void setGuests(String guests) {
        this.guests = guests;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
