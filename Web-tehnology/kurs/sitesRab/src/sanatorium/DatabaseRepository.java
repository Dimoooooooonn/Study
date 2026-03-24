package sanatorium;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DatabaseRepository {
    private final Path databaseFile;

    public DatabaseRepository(Path dataDir) throws IOException {
        Files.createDirectories(dataDir);
        this.databaseFile = dataDir.resolve("sanatorium.db");
        if (!Files.exists(databaseFile)) {
            save(new DatabaseData());
        }
        seedIfEmpty();
    }

    public synchronized List<User> getUsers() throws IOException {
        return new ArrayList<>(read().getUsers());
    }

    public synchronized User findUserById(int id) throws IOException {
        for (User user : read().getUsers()) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public synchronized User findUserByLogin(String login) throws IOException {
        String value = normalize(login);
        for (User user : read().getUsers()) {
            if (normalize(user.getEmail()).equals(value) || normalize(user.getPhone()).equals(value)) {
                return user;
            }
        }
        return null;
    }

    public synchronized boolean userExists(String email, String phone) throws IOException {
        String emailValue = normalize(email);
        String phoneValue = normalize(phone);
        for (User user : read().getUsers()) {
            if (!emailValue.isEmpty() && normalize(user.getEmail()).equals(emailValue)) {
                return true;
            }
            if (!phoneValue.isEmpty() && normalize(user.getPhone()).equals(phoneValue)) {
                return true;
            }
        }
        return false;
    }

    public synchronized User addUser(User user) throws IOException {
        DatabaseData data = read();
        user.setId(data.getNextUserId());
        data.setNextUserId(data.getNextUserId() + 1);
        data.getUsers().add(user);
        save(data);
        return user;
    }

    public synchronized void updateUser(User updated) throws IOException {
        DatabaseData data = read();
        for (int i = 0; i < data.getUsers().size(); i++) {
            if (data.getUsers().get(i).getId() == updated.getId()) {
                data.getUsers().set(i, updated);
                break;
            }
        }
        save(data);
    }

    public synchronized void updatePassword(int userId, String passwordHash) throws IOException {
        DatabaseData data = read();
        for (User user : data.getUsers()) {
            if (user.getId() == userId) {
                user.setPasswordHash(passwordHash);
            }
        }
        save(data);
    }

    public synchronized List<Booking> getBookingsByUserId(int userId) throws IOException {
        List<Booking> result = new ArrayList<>();
        for (Booking booking : read().getBookings()) {
            if (booking.getUserId() == userId) {
                result.add(booking);
            }
        }
        result.sort(Comparator.comparing(Booking::getCreatedAt).reversed());
        return result;
    }

    public synchronized Booking findBookingById(int bookingId) throws IOException {
        for (Booking booking : read().getBookings()) {
            if (booking.getId() == bookingId) {
                return booking;
            }
        }
        return null;
    }

    public synchronized Booking addBooking(Booking booking) throws IOException {
        DatabaseData data = read();
        booking.setId(data.getNextBookingId());
        data.setNextBookingId(data.getNextBookingId() + 1);
        data.getBookings().add(booking);
        save(data);
        return booking;
    }

    public synchronized void updateBookingStatus(int bookingId, String status) throws IOException {
        DatabaseData data = read();
        for (Booking booking : data.getBookings()) {
            if (booking.getId() == bookingId) {
                booking.setStatus(status);
            }
        }
        save(data);
    }

    public synchronized List<Payment> getPaymentsByUserId(int userId) throws IOException {
        List<Payment> result = new ArrayList<>();
        for (Payment payment : read().getPayments()) {
            if (payment.getUserId() == userId) {
                result.add(payment);
            }
        }
        result.sort(Comparator.comparing(Payment::getCreatedAt).reversed());
        return result;
    }

    public synchronized List<Payment> getPaymentsByBookingId(int bookingId) throws IOException {
        List<Payment> result = new ArrayList<>();
        for (Payment payment : read().getPayments()) {
            if (payment.getBookingId() == bookingId) {
                result.add(payment);
            }
        }
        result.sort(Comparator.comparing(Payment::getId));
        return result;
    }

    public synchronized void addPayment(Payment payment) throws IOException {
        DatabaseData data = read();
        payment.setId(data.getNextPaymentId());
        data.setNextPaymentId(data.getNextPaymentId() + 1);
        data.getPayments().add(payment);
        save(data);
    }

    public synchronized boolean payPayment(int paymentId, int userId) throws IOException {
        DatabaseData data = read();
        Payment current = null;
        for (Payment payment : data.getPayments()) {
            if (payment.getId() == paymentId && payment.getUserId() == userId) {
                current = payment;
                break;
            }
        }

        if (current == null) {
            return false;
        }

        if ("Оплачено".equalsIgnoreCase(current.getStatus())) {
            return true;
        }

        current.setStatus("Оплачено");
        current.setPaidAt(LocalDate.now().toString());
        updateBookingStatusInside(data, current.getBookingId());
        save(data);
        return true;
    }

    public synchronized int countPaidAmountByBookingId(int bookingId) throws IOException {
        int sum = 0;
        for (Payment payment : getPaymentsByBookingId(bookingId)) {
            if ("Оплачено".equalsIgnoreCase(payment.getStatus())) {
                sum += payment.getAmount();
            }
        }
        return sum;
    }

    public synchronized List<Review> getReviews() throws IOException {
        List<Review> result = new ArrayList<>(read().getReviews());
        result.sort(Comparator.comparing(Review::getCreatedAt).reversed());
        return result;
    }

    public synchronized void addReview(Review review) throws IOException {
        DatabaseData data = read();
        Review saved = new Review(data.getNextReviewId(), review.getName(), review.getRating(), review.getText(), review.getCreatedAt());
        data.setNextReviewId(data.getNextReviewId() + 1);
        data.getReviews().add(saved);
        save(data);
    }

    public synchronized List<SupportRequest> getSupportRequestsByUserId(int userId) throws IOException {
        List<SupportRequest> result = new ArrayList<>();
        for (SupportRequest request : read().getSupportRequests()) {
            if (request.getUserId() == userId) {
                result.add(request);
            }
        }
        result.sort(Comparator.comparing(SupportRequest::getCreatedAt).reversed());
        return result;
    }

    public synchronized void addSupportRequest(SupportRequest request) throws IOException {
        DatabaseData data = read();
        SupportRequest saved = new SupportRequest(data.getNextSupportId(), request.getUserId(), request.getCreatedAt(), request.getSubject(),
                request.getCategory(), request.getMessage(), request.getStatus(), request.getAnswer());
        data.setNextSupportId(data.getNextSupportId() + 1);
        data.getSupportRequests().add(saved);
        save(data);
    }

    private void seedIfEmpty() throws IOException {
        DatabaseData data = read();
        if (!data.getUsers().isEmpty()) {
            return;
        }

        User user = new User(
                data.getNextUserId(),
                "Иван",
                "Иванов",
                "guest@example.com",
                "+79001234567",
                PasswordUtils.hash("123456"),
                "1985-07-12",
                "Казань",
                "Предпочитаю тихий этаж и одну большую кровать."
        );
        data.setNextUserId(data.getNextUserId() + 1);
        data.getUsers().add(user);

        Booking booking1 = new Booking(data.getNextBookingId(), 1, "2026-04-10", "2026-04-17", "2", "Оздоровительный отдых",
                "Стандарт", "Иван Иванов", "+79001234567", "guest@example.com",
                "Нужен тихий этаж", "Трансфер", "Частично оплачено", "2026-03-15", 52500);
        data.setNextBookingId(data.getNextBookingId() + 1);
        data.getBookings().add(booking1);

        Booking booking2 = new Booking(data.getNextBookingId(), 1, "2025-11-02", "2025-11-05", "2", "Короткий восстановительный заезд",
                "Комфорт", "Иван Иванов", "+79001234567", "guest@example.com",
                "Осенний отдых", "", "Завершено", "2025-10-10", 29400);
        data.setNextBookingId(data.getNextBookingId() + 1);
        data.getBookings().add(booking2);

        Booking booking3 = new Booking(data.getNextBookingId(), 1, "2025-06-18", "2025-06-20", "1", "Оздоровительный отдых",
                "Стандарт", "Иван Иванов", "+79001234567", "guest@example.com",
                "Нужен номер поближе к парку", "", "Завершено", "2025-05-30", 15000);
        data.setNextBookingId(data.getNextBookingId() + 1);
        data.getBookings().add(booking3);

        addSeedPayment(data, 1, booking1.getId(), "2026-03-15", "Предоплата", 20000, "Оплачено", "2026-03-15");
        addSeedPayment(data, 1, booking1.getId(), "2026-03-22", "Остаток", 32500, "Ожидает оплаты", "");
        addSeedPayment(data, 1, booking2.getId(), "2025-10-10", "Полная оплата", 29400, "Оплачено", "2025-10-10");
        addSeedPayment(data, 1, booking3.getId(), "2025-05-30", "Полная оплата", 15000, "Оплачено", "2025-05-30");

        data.getReviews().add(new Review(data.getNextReviewId(), "Марина, 52 года", 5,
                "Приезжала на неделю отдыха. Всё организовано спокойно: питание по времени, процедуры без суеты, в кабинете удобно проверять записи.",
                "2026-03-01"));
        data.setNextReviewId(data.getNextReviewId() + 1);
        data.getReviews().add(new Review(data.getNextReviewId(), "Владимир, 61 год", 5,
                "Понравился бассейн и персонал лечебного корпуса. Номер тихий, уборка аккуратная, в расписании легко ориентироваться.",
                "2026-03-03"));
        data.setNextReviewId(data.getNextReviewId() + 1);
        data.getReviews().add(new Review(data.getNextReviewId(), "Елена, 44 года", 4,
                "Хороший баланс между процедурами и отдыхом. Хотелось бы чуть больше вечерних мероприятий, но в целом впечатление очень приятное.",
                "2026-03-05"));
        data.setNextReviewId(data.getNextReviewId() + 1);

        data.getSupportRequests().add(new SupportRequest(data.getNextSupportId(), 1, "2026-03-21", "Уточнение времени заезда", "Бронирование",
                "Нужен ранний заезд, если номер будет готов.", "Закрыто",
                "Подтверждён ранний заезд при наличии свободного номера."));
        data.setNextSupportId(data.getNextSupportId() + 1);
        data.getSupportRequests().add(new SupportRequest(data.getNextSupportId(), 1, "2026-03-23", "Диетическое питание", "Проживание",
                "Прошу отметить в профиле диетическое меню.", "В работе",
                "Передано в службу размещения."));
        data.setNextSupportId(data.getNextSupportId() + 1);

        save(data);
    }

    private void addSeedPayment(DatabaseData data, int userId, int bookingId, String createdAt, String title, int amount, String status, String paidAt) {
        Payment payment = new Payment(data.getNextPaymentId(), userId, bookingId, createdAt, title, amount, status, paidAt);
        data.setNextPaymentId(data.getNextPaymentId() + 1);
        data.getPayments().add(payment);
    }

    private void updateBookingStatusInside(DatabaseData data, int bookingId) {
        int total = 0;
        int paid = 0;
        for (Payment payment : data.getPayments()) {
            if (payment.getBookingId() == bookingId) {
                total += payment.getAmount();
                if ("Оплачено".equalsIgnoreCase(payment.getStatus())) {
                    paid += payment.getAmount();
                }
            }
        }

        for (Booking booking : data.getBookings()) {
            if (booking.getId() == bookingId) {
                if (total == 0) {
                    booking.setStatus("Новая заявка");
                } else if (paid == 0) {
                    booking.setStatus("Ожидает оплаты");
                } else if (paid < total) {
                    booking.setStatus("Частично оплачено");
                } else {
                    booking.setStatus("Подтверждено");
                }
                break;
            }
        }
    }

    private DatabaseData read() throws IOException {
        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(databaseFile))) {
            Object object = input.readObject();
            if (object instanceof DatabaseData data) {
                return data;
            }
            return new DatabaseData();
        } catch (ClassNotFoundException e) {
            throw new IOException("Не удалось прочитать базу данных", e);
        }
    }

    private void save(DatabaseData data) throws IOException {
        try (ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(databaseFile))) {
            output.writeObject(data);
        }
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.toLowerCase().replace(" ", "").trim();
    }
}
