package sanatorium;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseRepository {
    private final String host;
    private final String port;
    private final String databaseName;
    private final String user;
    private final String password;
    private final String serverUrl;
    private final String databaseUrl;

    public DatabaseRepository(Path configFile) throws IOException {
        createDefaultConfigIfMissing(configFile);

        Properties properties = new Properties();
        try {
            properties.load(Files.newBufferedReader(configFile, StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IOException("Не удалось прочитать файл настроек БД: " + configFile, e);
        }

        host = properties.getProperty("db.host", "localhost").trim();
        port = properties.getProperty("db.port", "3306").trim();
        databaseName = properties.getProperty("db.name", "sanatorium_db").trim();
        user = properties.getProperty("db.user", "root").trim();
        password = properties.getProperty("db.password", "");

        if (!databaseName.matches("[a-zA-Z0-9_]+")) {
            throw new IOException("Имя базы данных должно состоять только из букв, цифр и _. Сейчас: " + databaseName);
        }

        serverUrl = "jdbc:mysql://" + host + ":" + port + "/?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
        databaseUrl = "jdbc:mysql://" + host + ":" + port + "/" + databaseName + "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";

        loadDriver();
        initDatabase();
        createTables();
        seedIfEmpty();
    }

    private void createDefaultConfigIfMissing(Path configFile) throws IOException {
        Files.createDirectories(configFile.getParent());
        if (Files.exists(configFile)) {
            return;
        }

        String text = String.join(System.lineSeparator(),
                "# Настройки подключения к MySQL / MariaDB",
                "db.host=localhost",
                "db.port=3306",
                "db.name=sanatorium_db",
                "db.user=root",
                "db.password=",
                "");
        Files.writeString(configFile, text, StandardCharsets.UTF_8);
    }

    private void loadDriver() throws IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IOException("Не найден MySQL JDBC driver. Запускай проект через Maven, чтобы зависимость подтянулась автоматически.", e);
        }
    }

    private void initDatabase() throws IOException {
        try (Connection connection = openServerConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + databaseName + "` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
        } catch (SQLException e) {
            throw new IOException(buildConnectionMessage(e), e);
        }
    }

    private void createTables() throws IOException {
        try (Connection connection = openConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS users (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        first_name VARCHAR(100) NOT NULL,
                        last_name VARCHAR(100) NOT NULL,
                        email VARCHAR(190) NOT NULL,
                        phone VARCHAR(50) NOT NULL,
                        password_hash VARCHAR(255) NOT NULL,
                        birth_date DATE NULL,
                        city VARCHAR(150) NOT NULL DEFAULT '',
                        comment TEXT NULL,
                        UNIQUE KEY uq_users_email (email),
                        UNIQUE KEY uq_users_phone (phone)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
                    """);

            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS bookings (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        user_id INT NOT NULL,
                        check_in DATE NOT NULL,
                        check_out DATE NOT NULL,
                        guests VARCHAR(20) NOT NULL,
                        purpose VARCHAR(255) NOT NULL,
                        room VARCHAR(100) NOT NULL,
                        full_name VARCHAR(255) NOT NULL,
                        phone VARCHAR(50) NOT NULL,
                        email VARCHAR(190) NOT NULL,
                        comment TEXT NULL,
                        options_text TEXT NULL,
                        status VARCHAR(100) NOT NULL,
                        created_at DATE NOT NULL,
                        total_price INT NOT NULL,
                        CONSTRAINT fk_bookings_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
                    """);

            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS payments (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        user_id INT NOT NULL,
                        booking_id INT NOT NULL,
                        created_at DATE NOT NULL,
                        title VARCHAR(255) NOT NULL,
                        amount INT NOT NULL,
                        status VARCHAR(100) NOT NULL,
                        paid_at DATE NULL,
                        CONSTRAINT fk_payments_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                        CONSTRAINT fk_payments_booking FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
                    """);

            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS reviews (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(150) NOT NULL,
                        rating INT NOT NULL,
                        text TEXT NOT NULL,
                        created_at DATE NOT NULL
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
                    """);

            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS support_requests (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        user_id INT NOT NULL,
                        created_at DATE NOT NULL,
                        subject VARCHAR(255) NOT NULL,
                        category VARCHAR(100) NOT NULL,
                        message TEXT NOT NULL,
                        status VARCHAR(100) NOT NULL,
                        answer TEXT NULL,
                        CONSTRAINT fk_support_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
                    """);

        } catch (SQLException e) {
            throw new IOException("Не удалось создать таблицы в MySQL: " + e.getMessage(), e);
        }
    }

    public List<User> getUsers() throws IOException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY id";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                list.add(mapUser(resultSet));
            }
            return list;
        } catch (SQLException e) {
            throw new IOException("Не удалось получить пользователей: " + e.getMessage(), e);
        }
    }

    public User findUserById(int id) throws IOException {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new IOException("Не удалось найти пользователя: " + e.getMessage(), e);
        }
    }

    public User findUserByLogin(String login) throws IOException {
        String value = normalize(login);
        String sql = "SELECT * FROM users WHERE LOWER(email) = ? OR phone = ? LIMIT 1";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, value);
            statement.setString(2, login == null ? "" : login.trim());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new IOException("Не удалось найти пользователя по логину: " + e.getMessage(), e);
        }
    }

    public boolean userExists(String email, String phone) throws IOException {
        String sql = "SELECT id FROM users WHERE LOWER(email) = ? OR phone = ? LIMIT 1";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, normalize(email));
            statement.setString(2, phone == null ? "" : phone.trim());
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new IOException("Не удалось проверить пользователя: " + e.getMessage(), e);
        }
    }

    public User addUser(User userData) throws IOException {
        String sql = "INSERT INTO users (first_name, last_name, email, phone, password_hash, birth_date, city, comment) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, userData.getFirstName());
            statement.setString(2, userData.getLastName());
            statement.setString(3, userData.getEmail());
            statement.setString(4, userData.getPhone());
            statement.setString(5, userData.getPasswordHash());
            setNullableDate(statement, 6, userData.getBirthDate());
            statement.setString(7, userData.getCity());
            statement.setString(8, userData.getComment());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    userData.setId(keys.getInt(1));
                }
            }
            return userData;
        } catch (SQLException e) {
            throw new IOException("Не удалось добавить пользователя: " + simplifySqlMessage(e), e);
        }
    }

    public void updateUser(User updated) throws IOException {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, email = ?, phone = ?, birth_date = ?, city = ?, comment = ? WHERE id = ?";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, updated.getFirstName());
            statement.setString(2, updated.getLastName());
            statement.setString(3, updated.getEmail());
            statement.setString(4, updated.getPhone());
            setNullableDate(statement, 5, updated.getBirthDate());
            statement.setString(6, updated.getCity());
            statement.setString(7, updated.getComment());
            statement.setInt(8, updated.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IOException("Не удалось обновить профиль: " + simplifySqlMessage(e), e);
        }
    }

    public void updatePassword(int userId, String passwordHash) throws IOException {
        String sql = "UPDATE users SET password_hash = ? WHERE id = ?";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, passwordHash);
            statement.setInt(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IOException("Не удалось обновить пароль: " + e.getMessage(), e);
        }
    }

    public List<Booking> getBookingsByUserId(int userId) throws IOException {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE user_id = ? ORDER BY created_at DESC, id DESC";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    list.add(mapBooking(resultSet));
                }
            }
            return list;
        } catch (SQLException e) {
            throw new IOException("Не удалось получить бронирования: " + e.getMessage(), e);
        }
    }

    public Booking findBookingById(int bookingId) throws IOException {
        String sql = "SELECT * FROM bookings WHERE id = ?";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bookingId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapBooking(resultSet);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new IOException("Не удалось найти бронирование: " + e.getMessage(), e);
        }
    }

    public Booking addBooking(Booking booking) throws IOException {
        String sql = "INSERT INTO bookings (user_id, check_in, check_out, guests, purpose, room, full_name, phone, email, comment, options_text, status, created_at, total_price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, booking.getUserId());
            statement.setDate(2, Date.valueOf(booking.getCheckIn()));
            statement.setDate(3, Date.valueOf(booking.getCheckOut()));
            statement.setString(4, booking.getGuests());
            statement.setString(5, booking.getPurpose());
            statement.setString(6, booking.getRoom());
            statement.setString(7, booking.getFullName());
            statement.setString(8, booking.getPhone());
            statement.setString(9, booking.getEmail());
            statement.setString(10, booking.getComment());
            statement.setString(11, booking.getOptions());
            statement.setString(12, booking.getStatus());
            statement.setDate(13, Date.valueOf(booking.getCreatedAt()));
            statement.setInt(14, booking.getTotalPrice());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    booking.setId(keys.getInt(1));
                }
            }
            return booking;
        } catch (SQLException e) {
            throw new IOException("Не удалось сохранить бронирование: " + e.getMessage(), e);
        }
    }

    public void updateBookingStatus(int bookingId, String status) throws IOException {
        String sql = "UPDATE bookings SET status = ? WHERE id = ?";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setInt(2, bookingId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IOException("Не удалось обновить статус бронирования: " + e.getMessage(), e);
        }
    }

    public List<Payment> getPaymentsByUserId(int userId) throws IOException {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payments WHERE user_id = ? ORDER BY created_at DESC, id DESC";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    list.add(mapPayment(resultSet));
                }
            }
            return list;
        } catch (SQLException e) {
            throw new IOException("Не удалось получить платежи пользователя: " + e.getMessage(), e);
        }
    }

    public List<Payment> getPaymentsByBookingId(int bookingId) throws IOException {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payments WHERE booking_id = ? ORDER BY id";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bookingId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    list.add(mapPayment(resultSet));
                }
            }
            return list;
        } catch (SQLException e) {
            throw new IOException("Не удалось получить платежи бронирования: " + e.getMessage(), e);
        }
    }

    public void addPayment(Payment payment) throws IOException {
        String sql = "INSERT INTO payments (user_id, booking_id, created_at, title, amount, status, paid_at) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, payment.getUserId());
            statement.setInt(2, payment.getBookingId());
            statement.setDate(3, Date.valueOf(payment.getCreatedAt()));
            statement.setString(4, payment.getTitle());
            statement.setInt(5, payment.getAmount());
            statement.setString(6, payment.getStatus());
            setNullableDate(statement, 7, payment.getPaidAt());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    payment.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new IOException("Не удалось добавить платёж: " + e.getMessage(), e);
        }
    }

    public boolean payPayment(int paymentId, int userId) throws IOException {
        String findPaymentSql = "SELECT booking_id, status FROM payments WHERE id = ? AND user_id = ?";
        String updatePaymentSql = "UPDATE payments SET status = ?, paid_at = ? WHERE id = ?";
        String sumSql = "SELECT COALESCE(SUM(amount), 0) FROM payments WHERE booking_id = ? AND status = 'Оплачено'";
        String totalSql = "SELECT total_price FROM bookings WHERE id = ?";
        String updateBookingSql = "UPDATE bookings SET status = ? WHERE id = ?";

        try (Connection connection = openConnection()) {
            connection.setAutoCommit(false);
            try {
                int bookingId = 0;
                String currentStatus = "";

                try (PreparedStatement statement = connection.prepareStatement(findPaymentSql)) {
                    statement.setInt(1, paymentId);
                    statement.setInt(2, userId);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (!resultSet.next()) {
                            connection.rollback();
                            return false;
                        }
                        bookingId = resultSet.getInt("booking_id");
                        currentStatus = resultSet.getString("status");
                    }
                }

                if (!"Оплачено".equalsIgnoreCase(currentStatus)) {
                    try (PreparedStatement statement = connection.prepareStatement(updatePaymentSql)) {
                        statement.setString(1, "Оплачено");
                        statement.setDate(2, Date.valueOf(LocalDate.now()));
                        statement.setInt(3, paymentId);
                        statement.executeUpdate();
                    }
                }

                int paid = 0;
                try (PreparedStatement statement = connection.prepareStatement(sumSql)) {
                    statement.setInt(1, bookingId);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            paid = resultSet.getInt(1);
                        }
                    }
                }

                int total = 0;
                try (PreparedStatement statement = connection.prepareStatement(totalSql)) {
                    statement.setInt(1, bookingId);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            total = resultSet.getInt(1);
                        }
                    }
                }

                String bookingStatus = "Ожидает оплаты";
                if (paid > 0 && paid < total) {
                    bookingStatus = "Оплачено частично";
                }
                if (paid >= total && total > 0) {
                    bookingStatus = "Подтверждено";
                }

                try (PreparedStatement statement = connection.prepareStatement(updateBookingSql)) {
                    statement.setString(1, bookingStatus);
                    statement.setInt(2, bookingId);
                    statement.executeUpdate();
                }

                connection.commit();
                return true;
            } catch (Exception e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new IOException("Не удалось провести оплату: " + e.getMessage(), e);
        }
    }

    public int countPaidAmountByBookingId(int bookingId) throws IOException {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM payments WHERE booking_id = ? AND status = 'Оплачено'";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bookingId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
            return 0;
        } catch (SQLException e) {
            throw new IOException("Не удалось посчитать оплаченные суммы: " + e.getMessage(), e);
        }
    }

    public List<Review> getReviews() throws IOException {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT * FROM reviews ORDER BY created_at DESC, id DESC";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                list.add(mapReview(resultSet));
            }
            return list;
        } catch (SQLException e) {
            throw new IOException("Не удалось получить отзывы: " + e.getMessage(), e);
        }
    }

    public void addReview(Review review) throws IOException {
        String sql = "INSERT INTO reviews (name, rating, text, created_at) VALUES (?, ?, ?, ?)";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, review.getName());
            statement.setInt(2, review.getRating());
            statement.setString(3, review.getText());
            statement.setDate(4, Date.valueOf(review.getCreatedAt()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IOException("Не удалось сохранить отзыв: " + e.getMessage(), e);
        }
    }

    public List<SupportRequest> getSupportRequestsByUserId(int userId) throws IOException {
        List<SupportRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM support_requests WHERE user_id = ? ORDER BY created_at DESC, id DESC";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    list.add(mapSupportRequest(resultSet));
                }
            }
            return list;
        } catch (SQLException e) {
            throw new IOException("Не удалось получить обращения: " + e.getMessage(), e);
        }
    }

    public void addSupportRequest(SupportRequest request) throws IOException {
        String sql = "INSERT INTO support_requests (user_id, created_at, subject, category, message, status, answer) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, request.getUserId());
            statement.setDate(2, Date.valueOf(request.getCreatedAt()));
            statement.setString(3, request.getSubject());
            statement.setString(4, request.getCategory());
            statement.setString(5, request.getMessage());
            statement.setString(6, request.getStatus());
            statement.setString(7, request.getAnswer());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IOException("Не удалось сохранить обращение: " + e.getMessage(), e);
        }
    }

    private void seedIfEmpty() throws IOException {
        String sql = "SELECT COUNT(*) FROM users";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return;
            }
        } catch (SQLException e) {
            throw new IOException("Не удалось проверить заполнение базы: " + e.getMessage(), e);
        }

        User user = new User(0, "Иван", "Иванов", "guest@example.com", "+79001234567",
                PasswordUtils.hash("123456"), "1985-07-12", "Казань",
                "Предпочитаю тихий этаж и одну большую кровать.");
        user = addUser(user);

        Booking booking = new Booking(0, user.getId(), "2026-04-10", "2026-04-17", "2", "Оздоровительный отдых",
                "Стандарт", "Иван Иванов", "+79001234567", "guest@example.com",
                "Нужен номер рядом с парком.", "Трансфер", "Оплачено частично",
                LocalDate.now().minusDays(5).toString(), 54000);
        booking = addBooking(booking);

        addPayment(new Payment(0, user.getId(), booking.getId(), LocalDate.now().minusDays(5).toString(),
                "Предоплата", 16200, "Оплачено", LocalDate.now().minusDays(4).toString()));
        addPayment(new Payment(0, user.getId(), booking.getId(), LocalDate.now().plusDays(2).toString(),
                "Остаток", 37800, "Ожидает оплаты", ""));

        addReview(new Review(0, "Елена", 5, "Понравился персонал, бассейн и спокойная атмосфера.", LocalDate.now().minusDays(10).toString()));
        addReview(new Review(0, "Сергей", 4, "Хороший номер и питание, приедем ещё раз.", LocalDate.now().minusDays(7).toString()));

        addSupportRequest(new SupportRequest(0, user.getId(), LocalDate.now().minusDays(1).toString(),
                "Нужен ранний заезд", "Бронирование", "Подскажите, можно ли приехать на два часа раньше?",
                "В работе", "Обращение получено, администратор уточняет наличие свободного номера."));
    }

    private Connection openServerConnection() throws SQLException {
        return DriverManager.getConnection(serverUrl, user, password);
    }

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection(databaseUrl, user, password);
    }

    private User mapUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("email"),
                resultSet.getString("phone"),
                resultSet.getString("password_hash"),
                getDate(resultSet, "birth_date"),
                resultSet.getString("city"),
                resultSet.getString("comment")
        );
    }

    private Booking mapBooking(ResultSet resultSet) throws SQLException {
        return new Booking(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                getDate(resultSet, "check_in"),
                getDate(resultSet, "check_out"),
                resultSet.getString("guests"),
                resultSet.getString("purpose"),
                resultSet.getString("room"),
                resultSet.getString("full_name"),
                resultSet.getString("phone"),
                resultSet.getString("email"),
                resultSet.getString("comment"),
                resultSet.getString("options_text"),
                resultSet.getString("status"),
                getDate(resultSet, "created_at"),
                resultSet.getInt("total_price")
        );
    }

    private Payment mapPayment(ResultSet resultSet) throws SQLException {
        return new Payment(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getInt("booking_id"),
                getDate(resultSet, "created_at"),
                resultSet.getString("title"),
                resultSet.getInt("amount"),
                resultSet.getString("status"),
                getDate(resultSet, "paid_at")
        );
    }

    private Review mapReview(ResultSet resultSet) throws SQLException {
        return new Review(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getInt("rating"),
                resultSet.getString("text"),
                getDate(resultSet, "created_at")
        );
    }

    private SupportRequest mapSupportRequest(ResultSet resultSet) throws SQLException {
        return new SupportRequest(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                getDate(resultSet, "created_at"),
                resultSet.getString("subject"),
                resultSet.getString("category"),
                resultSet.getString("message"),
                resultSet.getString("status"),
                resultSet.getString("answer")
        );
    }

    private void setNullableDate(PreparedStatement statement, int index, String value) throws SQLException {
        if (value == null || value.isBlank()) {
            statement.setNull(index, Types.DATE);
            return;
        }
        statement.setDate(index, Date.valueOf(value));
    }

    private String getDate(ResultSet resultSet, String column) throws SQLException {
        Date date = resultSet.getDate(column);
        if (date == null) {
            return "";
        }
        return date.toLocalDate().toString();
    }

    private String normalize(String text) {
        return text == null ? "" : text.trim().toLowerCase();
    }

    private String simplifySqlMessage(SQLException e) {
        String message = e.getMessage();
        if (message == null) {
            return "ошибка базы данных";
        }
        if (message.contains("uq_users_email") || message.toLowerCase().contains("for key 'uq_users_email'")) {
            return "такой email уже используется";
        }
        if (message.contains("uq_users_phone") || message.toLowerCase().contains("for key 'uq_users_phone'")) {
            return "такой телефон уже используется";
        }
        return message;
    }

    private String buildConnectionMessage(SQLException e) {
        return "Не удалось подключиться к MySQL. Проверь config/db.properties и убедись, что сервер MySQL запущен. Текст ошибки: " + e.getMessage();
    }
}
