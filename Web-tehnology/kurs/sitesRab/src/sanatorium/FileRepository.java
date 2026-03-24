package sanatorium;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FileRepository {
    private final Path usersFile;
    private final Path bookingsFile;
    private final Path reviewsFile;
    private final Path supportFile;

    public FileRepository(Path dataDir) throws IOException {
        Files.createDirectories(dataDir);
        this.usersFile = dataDir.resolve("users.txt");
        this.bookingsFile = dataDir.resolve("bookings.txt");
        this.reviewsFile = dataDir.resolve("reviews.txt");
        this.supportFile = dataDir.resolve("support.txt");

        createIfMissing(usersFile);
        createIfMissing(bookingsFile);
        createIfMissing(reviewsFile);
        createIfMissing(supportFile);

        seedIfEmpty();
    }

    private void createIfMissing(Path file) throws IOException {
        if (!Files.exists(file)) {
            Files.writeString(file, "", StandardCharsets.UTF_8);
        }
    }

    private void seedIfEmpty() throws IOException {
        if (readLines(usersFile).isEmpty()) {
            User user = new User(
                    1,
                    "Иван",
                    "Иванов",
                    "guest@example.com",
                    "+79001234567",
                    PasswordUtils.hash("123456"),
                    "1985-07-12",
                    "Казань",
                    "Предпочитаю тихий этаж и одну большую кровать."
            );
            saveUsers(List.of(user));
        }

        if (readLines(bookingsFile).isEmpty()) {
            List<Booking> list = new ArrayList<>();
            list.add(new Booking(1, 1, "2026-04-10", "2026-04-17", "2", "Оздоровительный отдых",
                    "Стандарт", "Иван Иванов", "+79001234567", "guest@example.com",
                    "Нужен тихий этаж", "Трансфер", "Подтверждено", "2026-03-15", 52500));
            list.add(new Booking(2, 1, "2025-11-02", "2025-11-05", "2", "Короткий восстановительный заезд",
                    "Комфорт", "Иван Иванов", "+79001234567", "guest@example.com",
                    "Осенний отдых", "", "Завершено", "2025-10-10", 29400));
            list.add(new Booking(3, 1, "2025-06-18", "2025-06-20", "1", "Оздоровительный отдых",
                    "Стандарт", "Иван Иванов", "+79001234567", "guest@example.com",
                    "Нужен номер поближе к парку", "", "Завершено", "2025-05-30", 15000));
            saveBookings(list);
        }

        if (readLines(reviewsFile).isEmpty()) {
            List<Review> list = new ArrayList<>();
            list.add(new Review(1, "Марина, 52 года", 5,
                    "Приезжала на неделю отдыха. Всё организовано спокойно: питание по времени, процедуры без суеты, в кабинете удобно проверять записи.",
                    "2026-03-01"));
            list.add(new Review(2, "Владимир, 61 год", 5,
                    "Понравился бассейн и персонал лечебного корпуса. Номер тихий, уборка аккуратная, в расписании легко ориентироваться.",
                    "2026-03-03"));
            list.add(new Review(3, "Елена, 44 года", 4,
                    "Хороший баланс между процедурами и отдыхом. Хотелось бы чуть больше вечерних мероприятий, но в целом впечатление очень приятное.",
                    "2026-03-05"));
            saveReviews(list);
        }

        if (readLines(supportFile).isEmpty()) {
            List<SupportRequest> list = new ArrayList<>();
            list.add(new SupportRequest(1, 1, "2026-03-21", "Уточнение времени заезда", "Бронирование",
                    "Нужен ранний заезд, если номер будет готов.", "Закрыто",
                    "Подтверждён ранний заезд при наличии свободного номера."));
            list.add(new SupportRequest(2, 1, "2026-03-23", "Диетическое питание", "Проживание",
                    "Прошу отметить в профиле диетическое меню.", "В работе",
                    "Передано в службу размещения."));
            saveSupportRequests(list);
        }
    }

    public synchronized List<User> getUsers() throws IOException {
        List<User> users = new ArrayList<>();
        for (String line : readLines(usersFile)) {
            String[] parts = line.split("\\|", -1);
            if (parts.length < 9) {
                continue;
            }
            User user = new User(
                    parseInt(parts[0]),
                    parts[1],
                    parts[2],
                    parts[3],
                    parts[4],
                    parts[5],
                    parts[6],
                    parts[7],
                    parts[8]
            );
            users.add(user);
        }
        return users;
    }

    public synchronized User findUserById(int id) throws IOException {
        for (User user : getUsers()) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public synchronized User findUserByLogin(String login) throws IOException {
        String value = normalize(login);
        for (User user : getUsers()) {
            if (normalize(user.getEmail()).equals(value) || normalize(user.getPhone()).equals(value)) {
                return user;
            }
        }
        return null;
    }

    public synchronized boolean userExists(String email, String phone) throws IOException {
        String emailValue = normalize(email);
        String phoneValue = normalize(phone);
        for (User user : getUsers()) {
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
        List<User> users = getUsers();
        user.setId(nextUserId(users));
        users.add(user);
        saveUsers(users);
        return user;
    }

    public synchronized void updateUser(User updated) throws IOException {
        List<User> users = getUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == updated.getId()) {
                users.set(i, updated);
                break;
            }
        }
        saveUsers(users);
    }

    public synchronized void updatePassword(int userId, String passwordHash) throws IOException {
        List<User> users = getUsers();
        for (User user : users) {
            if (user.getId() == userId) {
                user.setPasswordHash(passwordHash);
            }
        }
        saveUsers(users);
    }

    public synchronized List<Booking> getBookingsByUserId(int userId) throws IOException {
        List<Booking> result = new ArrayList<>();
        for (String line : readLines(bookingsFile)) {
            String[] parts = line.split("\\|", -1);
            if (parts.length < 15) {
                continue;
            }
            Booking booking = new Booking(
                    parseInt(parts[0]),
                    parseInt(parts[1]),
                    parts[2],
                    parts[3],
                    parts[4],
                    parts[5],
                    parts[6],
                    parts[7],
                    parts[8],
                    parts[9],
                    parts[10],
                    parts[11],
                    parts[12],
                    parts[13],
                    parseInt(parts[14])
            );
            if (booking.getUserId() == userId) {
                result.add(booking);
            }
        }

        result.sort(Comparator.comparing(Booking::getCheckIn).reversed());
        return result;
    }

    public synchronized void addBooking(Booking booking) throws IOException {
        List<Booking> bookings = getAllBookings();
        booking.setId(nextBookingId(bookings));
        bookings.add(booking);
        saveBookings(bookings);
    }

    public synchronized List<Review> getReviews() throws IOException {
        List<Review> result = new ArrayList<>();
        for (String line : readLines(reviewsFile)) {
            String[] parts = line.split("\\|", -1);
            if (parts.length < 5) {
                continue;
            }
            result.add(new Review(parseInt(parts[0]), parts[1], parseInt(parts[2]), parts[3], parts[4]));
        }
        result.sort(Comparator.comparing(Review::getCreatedAt).reversed());
        return result;
    }

    public synchronized void addReview(Review review) throws IOException {
        List<Review> reviews = getReviews();
        int nextId = 1;
        for (Review item : reviews) {
            nextId = Math.max(nextId, item.getId() + 1);
        }
        reviews.add(new Review(nextId, review.getName(), review.getRating(), review.getText(), review.getCreatedAt()));
        saveReviews(reviews);
    }

    public synchronized List<SupportRequest> getSupportRequestsByUserId(int userId) throws IOException {
        List<SupportRequest> result = new ArrayList<>();
        for (String line : readLines(supportFile)) {
            String[] parts = line.split("\\|", -1);
            if (parts.length < 8) {
                continue;
            }
            SupportRequest request = new SupportRequest(
                    parseInt(parts[0]),
                    parseInt(parts[1]),
                    parts[2],
                    parts[3],
                    parts[4],
                    parts[5],
                    parts[6],
                    parts[7]
            );
            if (request.getUserId() == userId) {
                result.add(request);
            }
        }
        result.sort(Comparator.comparing(SupportRequest::getCreatedAt).reversed());
        return result;
    }

    public synchronized void addSupportRequest(SupportRequest request) throws IOException {
        List<SupportRequest> requests = getAllSupportRequests();
        int nextId = 1;
        for (SupportRequest item : requests) {
            nextId = Math.max(nextId, item.getId() + 1);
        }
        requests.add(new SupportRequest(nextId, request.getUserId(), request.getCreatedAt(), request.getSubject(),
                request.getCategory(), request.getMessage(), request.getStatus(), request.getAnswer()));
        saveSupportRequests(requests);
    }

    private List<Booking> getAllBookings() throws IOException {
        List<Booking> result = new ArrayList<>();
        for (User user : getUsers()) {
            result.addAll(getBookingsByUserId(user.getId()));
        }
        return result;
    }

    private List<SupportRequest> getAllSupportRequests() throws IOException {
        List<SupportRequest> result = new ArrayList<>();
        for (String line : readLines(supportFile)) {
            String[] parts = line.split("\\|", -1);
            if (parts.length < 8) {
                continue;
            }
            result.add(new SupportRequest(
                    parseInt(parts[0]),
                    parseInt(parts[1]),
                    parts[2],
                    parts[3],
                    parts[4],
                    parts[5],
                    parts[6],
                    parts[7]
            ));
        }
        return result;
    }

    private void saveUsers(List<User> users) throws IOException {
        List<String> lines = new ArrayList<>();
        for (User user : users) {
            lines.add(user.getId() + "|" + clean(user.getFirstName()) + "|" + clean(user.getLastName()) + "|" +
                    clean(user.getEmail()) + "|" + clean(user.getPhone()) + "|" + clean(user.getPasswordHash()) + "|" +
                    clean(user.getBirthDate()) + "|" + clean(user.getCity()) + "|" + clean(user.getComment()));
        }
        Files.write(usersFile, lines, StandardCharsets.UTF_8);
    }

    private void saveBookings(List<Booking> bookings) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Booking booking : bookings) {
            lines.add(booking.getId() + "|" + booking.getUserId() + "|" + clean(booking.getCheckIn()) + "|" +
                    clean(booking.getCheckOut()) + "|" + clean(booking.getGuests()) + "|" + clean(booking.getPurpose()) + "|" +
                    clean(booking.getRoom()) + "|" + clean(booking.getFullName()) + "|" + clean(booking.getPhone()) + "|" +
                    clean(booking.getEmail()) + "|" + clean(booking.getComment()) + "|" + clean(booking.getOptions()) + "|" +
                    clean(booking.getStatus()) + "|" + clean(booking.getCreatedAt()) + "|" + booking.getTotalPrice());
        }
        Files.write(bookingsFile, lines, StandardCharsets.UTF_8);
    }

    private void saveReviews(List<Review> reviews) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Review review : reviews) {
            lines.add(review.getId() + "|" + clean(review.getName()) + "|" + review.getRating() + "|" +
                    clean(review.getText()) + "|" + clean(review.getCreatedAt()));
        }
        Files.write(reviewsFile, lines, StandardCharsets.UTF_8);
    }

    private void saveSupportRequests(List<SupportRequest> requests) throws IOException {
        List<String> lines = new ArrayList<>();
        for (SupportRequest request : requests) {
            lines.add(request.getId() + "|" + request.getUserId() + "|" + clean(request.getCreatedAt()) + "|" +
                    clean(request.getSubject()) + "|" + clean(request.getCategory()) + "|" +
                    clean(request.getMessage()) + "|" + clean(request.getStatus()) + "|" + clean(request.getAnswer()));
        }
        Files.write(supportFile, lines, StandardCharsets.UTF_8);
    }

    private List<String> readLines(Path file) throws IOException {
        List<String> result = Files.readAllLines(file, StandardCharsets.UTF_8);
        List<String> cleaned = new ArrayList<>();
        for (String line : result) {
            if (!line.isBlank()) {
                cleaned.add(line);
            }
        }
        return cleaned;
    }

    private int nextUserId(List<User> users) {
        int max = 0;
        for (User user : users) {
            max = Math.max(max, user.getId());
        }
        return max + 1;
    }

    private int nextBookingId(List<Booking> bookings) {
        int max = 0;
        for (Booking booking : bookings) {
            max = Math.max(max, booking.getId());
        }
        return max + 1;
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    private String normalize(String value) {
        return clean(value).toLowerCase().replace(" ", "");
    }

    private String clean(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\r", " ").replace("\n", " ").replace("|", "/").trim();
    }
}
