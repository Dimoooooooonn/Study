package sanatorium;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String passwordHash;
    private String birthDate;
    private String city;
    private String comment;

    public User() {
    }

    public User(int id, String firstName, String lastName, String email, String phone,
                String passwordHash, String birthDate, String city, String comment) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.birthDate = birthDate;
        this.city = city;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName == null ? "" : firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName == null ? "" : lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email == null ? "" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone == null ? "" : phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswordHash() {
        return passwordHash == null ? "" : passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getBirthDate() {
        return birthDate == null ? "" : birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getCity() {
        return city == null ? "" : city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getComment() {
        return comment == null ? "" : comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDisplayName() {
        String name = (getFirstName() + " " + getLastName()).trim();
        if (!name.isEmpty()) {
            return name;
        }
        if (!getEmail().isEmpty()) {
            return getEmail();
        }
        return getPhone();
    }
}
