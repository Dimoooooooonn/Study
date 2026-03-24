package sanatorium;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class SanatoriumServer {
    private final HttpServer server;
    private final DatabaseRepository repository;
    private final Path publicDir;
    private final Map<String, Integer> sessions = new ConcurrentHashMap<>();

    public SanatoriumServer(int port, Path projectDir) throws IOException {
        Path baseDir = projectDir.toAbsolutePath().normalize();
        this.publicDir = baseDir.resolve("public").normalize();
        this.repository = new DatabaseRepository(baseDir.resolve("data").normalize());
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        this.server.createContext("/", this::handleRequest);
        this.server.setExecutor(Executors.newCachedThreadPool());
    }

    public void start() {
        server.start();
    }

    private void handleRequest(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            if (path.equals("/")) {
                path = "/index.html";
            }

            if (method.equalsIgnoreCase("GET")) {
                if (path.equals("/login.html")) {
                    renderLoginPage(exchange);
                    return;
                }
                if (path.equals("/register.html")) {
                    renderRegisterPage(exchange);
                    return;
                }
                if (path.equals("/reset.html")) {
                    renderResetPage(exchange);
                    return;
                }
                if (path.equals("/reviews.html")) {
                    renderReviewsPage(exchange);
                    return;
                }
                if (path.equals("/booking.html")) {
                    renderBookingPage(exchange);
                    return;
                }
                if (path.equals("/cabinet/index.html")) {
                    renderCabinetIndex(exchange);
                    return;
                }
                if (path.equals("/cabinet/profile.html")) {
                    renderProfilePage(exchange);
                    return;
                }
                if (path.equals("/cabinet/bookings.html")) {
                    renderBookingsPage(exchange);
                    return;
                }
                if (path.equals("/cabinet/schedule.html")) {
                    renderCabinetSchedulePage(exchange);
                    return;
                }
                if (path.equals("/cabinet/payments.html")) {
                    renderPaymentsPage(exchange);
                    return;
                }
                if (path.equals("/cabinet/support.html")) {
                    renderSupportPage(exchange);
                    return;
                }
                if (path.equals("/logout")) {
                    handleLogout(exchange);
                    return;
                }

                serveStatic(exchange, path);
                return;
            }

            if (method.equalsIgnoreCase("POST")) {
                if (path.equals("/login")) {
                    handleLogin(exchange);
                    return;
                }
                if (path.equals("/register")) {
                    handleRegister(exchange);
                    return;
                }
                if (path.equals("/reset")) {
                    handleReset(exchange);
                    return;
                }
                if (path.equals("/reviews")) {
                    handleReview(exchange);
                    return;
                }
                if (path.equals("/booking")) {
                    handleBooking(exchange);
                    return;
                }
                if (path.equals("/cabinet/profile")) {
                    handleProfileUpdate(exchange);
                    return;
                }
                if (path.equals("/cabinet/support")) {
                    handleSupportRequest(exchange);
                    return;
                }
                if (path.equals("/cabinet/payments/pay")) {
                    handlePayment(exchange);
                    return;
                }
            }

            HttpUtils.sendNotFound(exchange);
        } catch (Exception e) {
            String html = "<h1>500</h1><p>Ошибка сервера: " + HtmlUtils.escape(e.getMessage()) + "</p>";
            HttpUtils.sendHtml(exchange, html);
        }
    }

    private void serveStatic(HttpExchange exchange, String path) throws IOException {
        Path file = publicDir.resolve(path.substring(1)).normalize();
        if (!file.startsWith(publicDir) || !Files.exists(file) || Files.isDirectory(file)) {
            HttpUtils.sendNotFound(exchange);
            return;
        }
        HttpUtils.sendBytes(exchange, HttpUtils.readFile(file), HttpUtils.contentType(file));
    }

    private void renderLoginPage(HttpExchange exchange) throws IOException {
        if (getCurrentUser(exchange) != null) {
            HttpUtils.redirect(exchange, "/cabinet/index.html");
            return;
        }

        Map<String, String> values = new HashMap<>();
        values.put("messageBlock", getMessageBlock(exchange));
        HttpUtils.sendHtml(exchange, renderTemplate("login.html", values));
    }

    private void renderRegisterPage(HttpExchange exchange) throws IOException {
        if (getCurrentUser(exchange) != null) {
            HttpUtils.redirect(exchange, "/cabinet/index.html");
            return;
        }

        Map<String, String> values = new HashMap<>();
        values.put("messageBlock", getMessageBlock(exchange));
        HttpUtils.sendHtml(exchange, renderTemplate("register.html", values));
    }

    private void renderResetPage(HttpExchange exchange) throws IOException {
        Map<String, String> values = new HashMap<>();
        values.put("messageBlock", getMessageBlock(exchange));
        HttpUtils.sendHtml(exchange, renderTemplate("reset.html", values));
    }

    private void renderReviewsPage(HttpExchange exchange) throws IOException {
        Map<String, String> values = new HashMap<>();
        values.put("messageBlock", getMessageBlock(exchange));
        values.put("reviewsCards", buildReviewsCards(repository.getReviews()));
        HttpUtils.sendHtml(exchange, renderTemplate("reviews.html", values));
    }

    private void renderBookingPage(HttpExchange exchange) throws IOException {
        User user = getCurrentUser(exchange);
        Map<String, String> values = new HashMap<>();
        String messageBlock = getMessageBlock(exchange);
        if (user == null && messageBlock.isEmpty()) {
            messageBlock = HtmlUtils.messageBlock("Для сохранения бронирования лучше сначала войти в личный кабинет.", false);
        }

        values.put("messageBlock", messageBlock);
        values.put("currentUserName", user == null ? "" : HtmlUtils.escape(user.getDisplayName()));
        values.put("currentUserPhone", user == null ? "" : HtmlUtils.escape(user.getPhone()));
        values.put("currentUserEmail", user == null ? "" : HtmlUtils.escape(user.getEmail()));
        HttpUtils.sendHtml(exchange, renderTemplate("booking.html", values));
    }

    private void renderCabinetIndex(HttpExchange exchange) throws IOException {
        User user = requireUser(exchange);
        if (user == null) {
            return;
        }

        List<Booking> bookings = repository.getBookingsByUserId(user.getId());
        Booking current = findCurrentBooking(bookings);
        List<ScheduleItem> schedule = createSchedule(current);
        String paymentStatus = buildPaymentStatusText(current);

        Map<String, String> values = new HashMap<>();
        values.put("messageBlock", getMessageBlock(exchange));
        values.put("userName", HtmlUtils.escape(user.getDisplayName()));
        values.put("activeBookingCount", HtmlUtils.escape(countActiveBookings(bookings)));
        values.put("nextEvent", HtmlUtils.escape(schedule.isEmpty() ? "Нет записей" : schedule.get(0).time));
        values.put("paymentStatus", HtmlUtils.escape(paymentStatus));
        values.put("currentTripDate", HtmlUtils.escape(current == null ? "Активных бронирований пока нет" : formatDateRange(current.getCheckIn(), current.getCheckOut())));
        values.put("currentTripInfo", HtmlUtils.escape(current == null ? "Когда появится первое бронирование, оно отобразится здесь." : buildBookingInfo(current)));
        values.put("eventList", buildEventList(schedule));
        HttpUtils.sendHtml(exchange, renderTemplate("cabinet/index.html", values));
    }

    private void renderProfilePage(HttpExchange exchange) throws IOException {
        User user = requireUser(exchange);
        if (user == null) {
            return;
        }

        Map<String, String> values = new HashMap<>();
        values.put("messageBlock", getMessageBlock(exchange));
        values.put("firstName", HtmlUtils.escape(user.getFirstName()));
        values.put("lastName", HtmlUtils.escape(user.getLastName()));
        values.put("phone", HtmlUtils.escape(user.getPhone()));
        values.put("email", HtmlUtils.escape(user.getEmail()));
        values.put("birthDate", HtmlUtils.escape(user.getBirthDate()));
        values.put("city", HtmlUtils.escape(user.getCity()));
        values.put("comment", HtmlUtils.escape(user.getComment()));
        values.put("emergencyContact", HtmlUtils.escape("Мария Иванова · +7 (900) 765-43-21"));
        HttpUtils.sendHtml(exchange, renderTemplate("cabinet/profile.html", values));
    }

    private void renderBookingsPage(HttpExchange exchange) throws IOException {
        User user = requireUser(exchange);
        if (user == null) {
            return;
        }

        List<Booking> bookings = repository.getBookingsByUserId(user.getId());
        Booking current = findCurrentBooking(bookings);

        Map<String, String> values = new HashMap<>();
        values.put("messageBlock", getMessageBlock(exchange));
        values.put("currentBookingPeriod", HtmlUtils.escape(current == null ? "Активных заявок нет" : "Заезд " + formatDateRange(current.getCheckIn(), current.getCheckOut())));
        values.put("currentBookingInfo", HtmlUtils.escape(current == null ? "Вы ещё не создавали бронирование." : buildBookingInfo(current)));
        values.put("bookingRows", buildBookingRows(bookings));
        HttpUtils.sendHtml(exchange, renderTemplate("cabinet/bookings.html", values));
    }

    private void renderCabinetSchedulePage(HttpExchange exchange) throws IOException {
        User user = requireUser(exchange);
        if (user == null) {
            return;
        }

        Booking current = findCurrentBooking(repository.getBookingsByUserId(user.getId()));
        List<ScheduleItem> schedule = createSchedule(current);

        Map<String, String> values = new HashMap<>();
        values.put("nextEventDescription", HtmlUtils.escape(schedule.isEmpty() ? "Пока нет процедур и мероприятий." : schedule.get(0).description));
        values.put("scheduleRows", buildScheduleRows(schedule));
        HttpUtils.sendHtml(exchange, renderTemplate("cabinet/schedule.html", values));
    }

    private void renderPaymentsPage(HttpExchange exchange) throws IOException {
        User user = requireUser(exchange);
        if (user == null) {
            return;
        }

        List<Booking> bookings = repository.getBookingsByUserId(user.getId());
        Booking current = findCurrentBooking(bookings);
        List<Payment> payments = current == null ? new ArrayList<>() : repository.getPaymentsByBookingId(current.getId());

        int total = current == null ? 0 : current.getTotalPrice();
        int paid = 0;
        for (Payment payment : payments) {
            if ("Оплачено".equalsIgnoreCase(payment.getStatus())) {
                paid += payment.getAmount();
            }
        }
        int left = Math.max(0, total - paid);

        Map<String, String> values = new HashMap<>();
        values.put("messageBlock", getMessageBlock(exchange));
        values.put("totalAmount", HtmlUtils.escape(formatMoney(total)));
        values.put("paidAmount", HtmlUtils.escape(formatMoney(paid)));
        values.put("leftAmount", HtmlUtils.escape(formatMoney(left)));
        values.put("currentBookingTitle", HtmlUtils.escape(current == null ? "Сейчас нет активного бронирования." : formatDateRange(current.getCheckIn(), current.getCheckOut()) + " · " + current.getRoom()));
        values.put("paymentRows", buildPaymentRows(payments));
        values.put("paymentDescription", HtmlUtils.escape(buildPaymentDescription(current, paid, left)));
        HttpUtils.sendHtml(exchange, renderTemplate("cabinet/payments.html", values));
    }

    private void renderSupportPage(HttpExchange exchange) throws IOException {
        User user = requireUser(exchange);
        if (user == null) {
            return;
        }

        Map<String, String> values = new HashMap<>();
        values.put("messageBlock", getMessageBlock(exchange));
        values.put("supportRows", buildSupportRows(repository.getSupportRequestsByUserId(user.getId())));
        HttpUtils.sendHtml(exchange, renderTemplate("cabinet/support.html", values));
    }

    private void handleLogin(HttpExchange exchange) throws IOException {
        Map<String, String> form = HttpUtils.parseForm(exchange);
        String login = form.getOrDefault("login", "").trim();
        String password = form.getOrDefault("password", "").trim();

        if (login.isEmpty() || password.isEmpty()) {
            redirectWithMessage(exchange, "/login.html", "Заполните логин и пароль.", false);
            return;
        }

        User user = repository.findUserByLogin(login);
        if (user == null || !user.getPasswordHash().equals(PasswordUtils.hash(password))) {
            redirectWithMessage(exchange, "/login.html", "Неверный логин или пароль.", false);
            return;
        }

        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, user.getId());
        exchange.getResponseHeaders().add("Set-Cookie", "SESSION_ID=" + sessionId + "; Path=/; HttpOnly");
        HttpUtils.redirect(exchange, "/cabinet/index.html?message=" + HttpUtils.encode("Вы успешно вошли в кабинет.") + "&status=ok");
    }

    private void handleRegister(HttpExchange exchange) throws IOException {
        Map<String, String> form = HttpUtils.parseForm(exchange);
        String firstName = form.getOrDefault("firstName", "").trim();
        String lastName = form.getOrDefault("lastName", "").trim();
        String email = form.getOrDefault("email", "").trim();
        String phone = form.getOrDefault("phone", "").trim();
        String password = form.getOrDefault("password", "").trim();
        String repeatPassword = form.getOrDefault("repeatPassword", "").trim();
        String comment = form.getOrDefault("comment", "").trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            redirectWithMessage(exchange, "/register.html", "Заполните все основные поля.", false);
            return;
        }

        if (!password.equals(repeatPassword)) {
            redirectWithMessage(exchange, "/register.html", "Пароли не совпадают.", false);
            return;
        }

        if (!form.containsKey("agree")) {
            redirectWithMessage(exchange, "/register.html", "Нужно подтвердить согласие на обработку данных.", false);
            return;
        }

        if (repository.userExists(email, phone)) {
            redirectWithMessage(exchange, "/register.html", "Пользователь с таким email или телефоном уже существует.", false);
            return;
        }

        User user = new User(0, firstName, lastName, email, phone, PasswordUtils.hash(password), "", "", comment);
        user = repository.addUser(user);

        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, user.getId());
        exchange.getResponseHeaders().add("Set-Cookie", "SESSION_ID=" + sessionId + "; Path=/; HttpOnly");
        HttpUtils.redirect(exchange, "/cabinet/index.html?message=" + HttpUtils.encode("Аккаунт создан.") + "&status=ok");
    }

    private void handleReset(HttpExchange exchange) throws IOException {
        Map<String, String> form = HttpUtils.parseForm(exchange);
        String login = form.getOrDefault("login", "").trim();
        if (login.isEmpty()) {
            redirectWithMessage(exchange, "/reset.html", "Введите email или телефон.", false);
            return;
        }

        User user = repository.findUserByLogin(login);
        if (user == null) {
            redirectWithMessage(exchange, "/reset.html", "Пользователь не найден.", false);
            return;
        }

        String tempPassword = "12345678";
        repository.updatePassword(user.getId(), PasswordUtils.hash(tempPassword));
        HttpUtils.redirect(exchange, "/login.html?message=" +
                HttpUtils.encode("Временный пароль: " + tempPassword + ". После входа обновите данные в профиле.") +
                "&status=ok");
    }

    private void handleReview(HttpExchange exchange) throws IOException {
        Map<String, String> form = HttpUtils.parseForm(exchange);
        String name = form.getOrDefault("name", "").trim();
        String ratingText = form.getOrDefault("rating", "").trim();
        String reviewText = form.getOrDefault("review", "").trim();

        if (name.isEmpty() || ratingText.isEmpty() || reviewText.isEmpty()) {
            redirectWithMessage(exchange, "/reviews.html", "Заполните имя, оценку и отзыв.", false);
            return;
        }

        int rating = parseInt(ratingText);
        if (rating < 3 || rating > 5) {
            redirectWithMessage(exchange, "/reviews.html", "Оценка должна быть от 3 до 5.", false);
            return;
        }

        Review review = new Review(0, name, rating, reviewText, LocalDate.now().toString());
        repository.addReview(review);
        redirectWithMessage(exchange, "/reviews.html", "Спасибо, ваш отзыв сохранён.", true);
    }

    private void handleBooking(HttpExchange exchange) throws IOException {
        User user = getCurrentUser(exchange);
        if (user == null) {
            redirectWithMessage(exchange, "/login.html", "Сначала войдите в личный кабинет, чтобы сохранить бронирование.", false);
            return;
        }

        Map<String, String> form = HttpUtils.parseForm(exchange);
        String checkIn = form.getOrDefault("checkIn", "").trim();
        String checkOut = form.getOrDefault("checkOut", "").trim();
        String guests = form.getOrDefault("guests", "").trim();
        String purpose = form.getOrDefault("purpose", "").trim();
        String room = form.getOrDefault("room", "").trim();
        String fullName = form.getOrDefault("fullName", "").trim();
        String phone = form.getOrDefault("phone", "").trim();
        String email = form.getOrDefault("email", "").trim();
        String comment = form.getOrDefault("comment", "").trim();

        if (checkIn.isEmpty() || checkOut.isEmpty() || guests.isEmpty() || room.isEmpty() ||
                fullName.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            redirectWithMessage(exchange, "/booking.html", "Заполните все поля перед подтверждением.", false);
            return;
        }

        int total = calculateTotal(checkIn, checkOut, room, form);
        String options = buildOptionsText(form);

        Booking booking = new Booking(0, user.getId(), checkIn, checkOut, guests, purpose, room,
                fullName, phone, email, comment, options, "Ожидает оплаты",
                LocalDate.now().toString(), total);

        booking = repository.addBooking(booking);

        int prepayment = total * 30 / 100;
        if (prepayment < 1000) {
            prepayment = Math.min(total, 1000);
        }
        int left = total - prepayment;

        repository.addPayment(new Payment(0, user.getId(), booking.getId(), LocalDate.now().toString(),
                "Предоплата", prepayment, "Ожидает оплаты", ""));

        if (left > 0) {
            repository.addPayment(new Payment(0, user.getId(), booking.getId(), LocalDate.now().plusDays(7).toString(),
                    "Остаток", left, "Ожидает оплаты", ""));
        }

        redirectWithMessage(exchange, "/cabinet/payments.html", "Бронирование создано. По нему сформированы платежи.", true);
    }

    private void handleProfileUpdate(HttpExchange exchange) throws IOException {
        User user = requireUser(exchange);
        if (user == null) {
            return;
        }

        Map<String, String> form = HttpUtils.parseForm(exchange);
        user.setFirstName(form.getOrDefault("firstName", "").trim());
        user.setLastName(form.getOrDefault("lastName", "").trim());
        user.setPhone(form.getOrDefault("phone", "").trim());
        user.setEmail(form.getOrDefault("email", "").trim());
        user.setBirthDate(form.getOrDefault("birthDate", "").trim());
        user.setCity(form.getOrDefault("city", "").trim());
        user.setComment(form.getOrDefault("comment", "").trim());

        if (user.getFirstName().isEmpty() || user.getLastName().isEmpty() || user.getPhone().isEmpty() || user.getEmail().isEmpty()) {
            redirectWithMessage(exchange, "/cabinet/profile.html", "Имя, фамилия, телефон и email должны быть заполнены.", false);
            return;
        }

        repository.updateUser(user);
        redirectWithMessage(exchange, "/cabinet/profile.html", "Профиль сохранён.", true);
    }

    private void handleSupportRequest(HttpExchange exchange) throws IOException {
        User user = requireUser(exchange);
        if (user == null) {
            return;
        }

        Map<String, String> form = HttpUtils.parseForm(exchange);
        String subject = form.getOrDefault("subject", "").trim();
        String category = form.getOrDefault("category", "").trim();
        String message = form.getOrDefault("message", "").trim();

        if (subject.isEmpty() || category.isEmpty() || message.isEmpty()) {
            redirectWithMessage(exchange, "/cabinet/support.html", "Заполните тему, категорию и сообщение.", false);
            return;
        }

        SupportRequest request = new SupportRequest(0, user.getId(), LocalDate.now().toString(),
                subject, category, message, "В работе", "Обращение получено, мы свяжемся с вами.");
        repository.addSupportRequest(request);
        redirectWithMessage(exchange, "/cabinet/support.html", "Обращение отправлено.", true);
    }

    private void handlePayment(HttpExchange exchange) throws IOException {
        User user = requireUser(exchange);
        if (user == null) {
            return;
        }

        Map<String, String> form = HttpUtils.parseForm(exchange);
        int paymentId = parseInt(form.getOrDefault("paymentId", "0"));
        if (paymentId <= 0) {
            redirectWithMessage(exchange, "/cabinet/payments.html", "Не удалось определить платёж.", false);
            return;
        }

        boolean ok = repository.payPayment(paymentId, user.getId());
        if (!ok) {
            redirectWithMessage(exchange, "/cabinet/payments.html", "Платёж не найден.", false);
            return;
        }

        redirectWithMessage(exchange, "/cabinet/payments.html", "Оплата проведена. Статус обновлён.", true);
    }

    private void handleLogout(HttpExchange exchange) throws IOException {
        String sessionId = HttpUtils.getCookie(exchange, "SESSION_ID");
        if (!sessionId.isEmpty()) {
            sessions.remove(sessionId);
        }
        exchange.getResponseHeaders().add("Set-Cookie", "SESSION_ID=deleted; Path=/; Max-Age=0; HttpOnly");
        HttpUtils.redirect(exchange, "/login.html?message=" + HttpUtils.encode("Вы вышли из кабинета.") + "&status=ok");
    }

    private User getCurrentUser(HttpExchange exchange) throws IOException {
        String sessionId = HttpUtils.getCookie(exchange, "SESSION_ID");
        if (sessionId.isEmpty()) {
            return null;
        }
        Integer userId = sessions.get(sessionId);
        if (userId == null) {
            return null;
        }
        return repository.findUserById(userId);
    }

    private User requireUser(HttpExchange exchange) throws IOException {
        User user = getCurrentUser(exchange);
        if (user == null) {
            redirectWithMessage(exchange, "/login.html", "Сначала войдите в личный кабинет.", false);
            return null;
        }
        return user;
    }

    private String getMessageBlock(HttpExchange exchange) {
        Map<String, String> query = HttpUtils.parseQuery(exchange.getRequestURI().getRawQuery());
        String message = query.getOrDefault("message", "");
        String status = query.getOrDefault("status", "ok");
        return HtmlUtils.messageBlock(message, !"warn".equalsIgnoreCase(status));
    }

    private void redirectWithMessage(HttpExchange exchange, String path, String message, boolean ok) throws IOException {
        String status = ok ? "ok" : "warn";
        HttpUtils.redirect(exchange, path + "?message=" + HttpUtils.encode(message) + "&status=" + status);
    }

    private String renderTemplate(String relativePath, Map<String, String> values) throws IOException {
        String template = Files.readString(publicDir.resolve(relativePath), StandardCharsets.UTF_8);
        return TemplateUtils.render(template, values);
    }

    private Booking findCurrentBooking(List<Booking> bookings) {
        for (Booking booking : bookings) {
            if (!"Завершено".equalsIgnoreCase(booking.getStatus())) {
                return booking;
            }
        }
        if (bookings.isEmpty()) {
            return null;
        }
        return bookings.get(0);
    }

    private String countActiveBookings(List<Booking> bookings) {
        int count = 0;
        for (Booking booking : bookings) {
            if (!"Завершено".equalsIgnoreCase(booking.getStatus())) {
                count++;
            }
        }
        if (count == 0) {
            return "Нет поездок";
        }
        if (count == 1) {
            return "1 поездка";
        }
        return count + " поездки";
    }

    private String buildBookingInfo(Booking booking) {
        String options = booking.getOptions().isBlank() ? "без дополнений" : booking.getOptions();
        return booking.getRoom() + " · " + booking.getGuests() + " гостя · " + booking.getPurpose() + " · " + options;
    }

    private String buildPaymentStatusText(Booking booking) throws IOException {
        if (booking == null) {
            return "Нет данных";
        }

        List<Payment> payments = repository.getPaymentsByBookingId(booking.getId());
        if (payments.isEmpty()) {
            return "Счета ещё не сформированы";
        }

        int total = 0;
        int paid = 0;
        for (Payment payment : payments) {
            total += payment.getAmount();
            if ("Оплачено".equalsIgnoreCase(payment.getStatus())) {
                paid += payment.getAmount();
            }
        }

        if (paid == 0) {
            return "Ожидает оплаты";
        }
        if (paid < total) {
            return "Оплачено частично";
        }
        return "Оплачено полностью";
    }

    private String buildPaymentDescription(Booking booking, int paid, int left) {
        if (booking == null) {
            return "Когда появится бронирование, здесь будет видна сумма и статус оплаты.";
        }
        if (paid == 0) {
            return "После создания бронирования мы сформировали платежи. Их можно оплатить прямо на этой странице учебного проекта.";
        }
        if (left > 0) {
            return "Часть суммы уже оплачена. Остаток пока ждёт оплаты.";
        }
        return "Все платежи по текущему бронированию оплачены.";
    }

    private String buildReviewsCards(List<Review> reviews) {
        StringBuilder builder = new StringBuilder();
        for (Review review : reviews) {
            builder.append("<article class=\"card\">");
            builder.append("<h2 class=\"card__title\">").append(HtmlUtils.escape(review.getName())).append("</h2>");
            builder.append("<p class=\"card__meta\">")
                    .append(HtmlUtils.escape(HtmlUtils.stars(review.getRating())))
                    .append(" · ")
                    .append(HtmlUtils.escape(review.getText()))
                    .append("</p>");
            builder.append("</article>");
        }
        return builder.toString();
    }

    private String buildBookingRows(List<Booking> bookings) throws IOException {
        if (bookings.isEmpty()) {
            return "<tr><td colspan=\"4\">Бронирований пока нет.</td></tr>";
        }

        StringBuilder builder = new StringBuilder();
        for (Booking booking : bookings) {
            int paid = repository.countPaidAmountByBookingId(booking.getId());
            builder.append("<tr>");
            builder.append("<td>").append(HtmlUtils.escape(formatDateRange(booking.getCheckIn(), booking.getCheckOut()))).append("</td>");
            builder.append("<td>").append(HtmlUtils.escape(booking.getRoom())).append("</td>");
            builder.append("<td><span class=\"badge\">").append(HtmlUtils.escape(booking.getStatus())).append("</span></td>");
            builder.append("<td>").append(HtmlUtils.escape("Сумма: " + formatMoney(booking.getTotalPrice()) + " · оплачено: " + formatMoney(paid))).append("</td>");
            builder.append("</tr>");
        }
        return builder.toString();
    }

    private String buildScheduleRows(List<ScheduleItem> schedule) {
        if (schedule.isEmpty()) {
            return "<tr><td colspan=\"4\">Пока нет событий в расписании.</td></tr>";
        }

        StringBuilder builder = new StringBuilder();
        for (ScheduleItem item : schedule) {
            builder.append("<tr>");
            builder.append("<td>").append(HtmlUtils.escape(item.time)).append("</td>");
            builder.append("<td>").append(HtmlUtils.escape(item.type)).append("</td>");
            builder.append("<td>").append(HtmlUtils.escape(item.title)).append("</td>");
            builder.append("<td><span class=\"badge\">").append(HtmlUtils.escape(item.status)).append("</span></td>");
            builder.append("</tr>");
        }
        return builder.toString();
    }

    private String buildEventList(List<ScheduleItem> schedule) {
        if (schedule.isEmpty()) {
            return "<li>Пока нет событий</li>";
        }

        StringBuilder builder = new StringBuilder();
        for (ScheduleItem item : schedule) {
            builder.append("<li>")
                    .append(HtmlUtils.escape(item.time + " — " + item.title))
                    .append("</li>");
        }
        return builder.toString();
    }

    private String buildPaymentRows(List<Payment> payments) {
        if (payments.isEmpty()) {
            return "<tr><td colspan=\"4\">По этому бронированию пока нет платежей.</td></tr>";
        }

        StringBuilder builder = new StringBuilder();
        for (Payment payment : payments) {
            builder.append("<tr>");
            builder.append("<td>").append(HtmlUtils.escape(payment.getCreatedAt())).append("</td>");
            builder.append("<td>").append(HtmlUtils.escape(payment.getTitle())).append("</td>");
            builder.append("<td>").append(HtmlUtils.escape(formatMoney(payment.getAmount()))).append("</td>");
            builder.append("<td>");
            builder.append("<span class=\"badge\">").append(HtmlUtils.escape(payment.getStatus())).append("</span>");
            if (!"Оплачено".equalsIgnoreCase(payment.getStatus())) {
                builder.append("<form action=\"/cabinet/payments/pay\" method=\"post\" style=\"margin-top:8px\">");
                builder.append("<input type=\"hidden\" name=\"paymentId\" value=\"").append(payment.getId()).append("\">");
                builder.append("<button class=\"btn btn--sm btn--primary\" type=\"submit\">Оплатить</button>");
                builder.append("</form>");
            } else if (!payment.getPaidAt().isBlank()) {
                builder.append("<div class=\"muted small\" style=\"margin-top:8px\">Оплачено: ")
                        .append(HtmlUtils.escape(payment.getPaidAt()))
                        .append("</div>");
            }
            builder.append("</td>");
            builder.append("</tr>");
        }
        return builder.toString();
    }

    private String buildSupportRows(List<SupportRequest> requests) {
        if (requests.isEmpty()) {
            return "<tr><td colspan=\"4\">Обращений пока нет.</td></tr>";
        }

        StringBuilder builder = new StringBuilder();
        for (SupportRequest request : requests) {
            builder.append("<tr>");
            builder.append("<td>").append(HtmlUtils.escape(request.getCreatedAt())).append("</td>");
            builder.append("<td>").append(HtmlUtils.escape(request.getSubject())).append("</td>");
            builder.append("<td><span class=\"badge\">").append(HtmlUtils.escape(request.getStatus())).append("</span></td>");
            builder.append("<td>").append(HtmlUtils.escape(request.getAnswer())).append("</td>");
            builder.append("</tr>");
        }
        return builder.toString();
    }

    private List<ScheduleItem> createSchedule(Booking booking) {
        List<ScheduleItem> list = new ArrayList<>();
        if (booking == null) {
            return list;
        }

        list.add(new ScheduleItem("09:00", "Активность", "Прогулка по парку", "Запланировано",
                "Сегодня в 09:00 — прогулка по парку, маршрут у озера."));
        list.add(new ScheduleItem("11:30", "Процедура", "Массаж спины", "Подтверждено",
                "Сегодня в 11:30 — массаж спины, кабинет №4, лечебный корпус."));
        list.add(new ScheduleItem("15:00", "ЛФК", "Занятие в малой группе", "Запланировано",
                "Сегодня в 15:00 — занятие ЛФК в малой группе."));
        list.add(new ScheduleItem("18:00", "Лекция", "Качество сна и восстановление", "Свободная запись",
                "Сегодня в 18:00 — лекция о качестве сна."));
        return list;
    }

    private int calculateTotal(String checkIn, String checkOut, String room, Map<String, String> form) {
        int roomPrice = switch (room) {
            case "Комфорт" -> 9800;
            case "Семейный" -> 12400;
            default -> 7500;
        };

        long nights = 1;
        try {
            LocalDate in = LocalDate.parse(checkIn);
            LocalDate out = LocalDate.parse(checkOut);
            nights = ChronoUnit.DAYS.between(in, out);
            if (nights < 1) {
                nights = 1;
            }
        } catch (Exception ignored) {
        }

        int total = (int) nights * roomPrice;
        if (form.containsKey("transfer")) {
            total += 1500;
        }
        if (form.containsKey("massage")) {
            total += 4000;
        }
        if (form.containsKey("lateCheckout")) {
            total += 2000;
        }
        return total;
    }

    private String buildOptionsText(Map<String, String> form) {
        List<String> list = new ArrayList<>();
        if (form.containsKey("transfer")) {
            list.add("Трансфер");
        }
        if (form.containsKey("massage")) {
            list.add("Дополнительный массаж");
        }
        if (form.containsKey("lateCheckout")) {
            list.add("Поздний выезд");
        }
        return list.isEmpty() ? "" : String.join(", ", list);
    }

    private String formatMoney(int amount) {
        String value = String.valueOf(amount);
        StringBuilder builder = new StringBuilder(value);
        for (int i = builder.length() - 3; i > 0; i -= 3) {
            builder.insert(i, ' ');
        }
        return builder + " ₽";
    }

    private String formatDateRange(String start, String end) {
        try {
            LocalDate first = LocalDate.parse(start);
            LocalDate second = LocalDate.parse(end);
            if (first.getYear() == second.getYear() && first.getMonth() == second.getMonth()) {
                return first.getDayOfMonth() + "–" + second.getDayOfMonth() + " " + monthName(first.getMonthValue()) + " " + first.getYear();
            }
            return first.getDayOfMonth() + " " + monthName(first.getMonthValue()) + " — " +
                    second.getDayOfMonth() + " " + monthName(second.getMonthValue()) + " " + second.getYear();
        } catch (Exception e) {
            return start + " — " + end;
        }
    }

    private String monthName(int month) {
        return switch (month) {
            case 1 -> "января";
            case 2 -> "февраля";
            case 3 -> "марта";
            case 4 -> "апреля";
            case 5 -> "мая";
            case 6 -> "июня";
            case 7 -> "июля";
            case 8 -> "августа";
            case 9 -> "сентября";
            case 10 -> "октября";
            case 11 -> "ноября";
            case 12 -> "декабря";
            default -> "";
        };
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    private static class ScheduleItem {
        private final String time;
        private final String type;
        private final String title;
        private final String status;
        private final String description;

        private ScheduleItem(String time, String type, String title, String status, String description) {
            this.time = time;
            this.type = type;
            this.title = title;
            this.status = status;
            this.description = description;
        }
    }
}
