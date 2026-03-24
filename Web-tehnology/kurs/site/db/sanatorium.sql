CREATE DATABASE IF NOT EXISTS `sanatorium_db`
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE `sanatorium_db`;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS reviews (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    rating INT NOT NULL,
    text TEXT NOT NULL,
    created_at DATE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT IGNORE INTO users (id, first_name, last_name, email, phone, password_hash, birth_date, city, comment)
VALUES
(1, 'Иван', 'Иванов', 'guest@example.com', '+79001234567', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '1985-07-12', 'Казань', 'Предпочитаю тихий этаж и одну большую кровать.');

INSERT IGNORE INTO bookings (id, user_id, check_in, check_out, guests, purpose, room, full_name, phone, email, comment, options_text, status, created_at, total_price)
VALUES
(1, 1, '2026-04-10', '2026-04-17', '2', 'Оздоровительный отдых', 'Стандарт', 'Иван Иванов', '+79001234567', 'guest@example.com', 'Нужен номер рядом с парком.', 'Трансфер', 'Оплачено частично', CURDATE(), 54000);

INSERT IGNORE INTO payments (id, user_id, booking_id, created_at, title, amount, status, paid_at)
VALUES
(1, 1, 1, CURDATE(), 'Предоплата', 16200, 'Оплачено', CURDATE()),
(2, 1, 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'Остаток', 37800, 'Ожидает оплаты', NULL);

INSERT IGNORE INTO reviews (id, name, rating, text, created_at)
VALUES
(1, 'Елена', 5, 'Понравился персонал, бассейн и спокойная атмосфера.', CURDATE()),
(2, 'Сергей', 4, 'Хороший номер и питание, приедем ещё раз.', CURDATE());

INSERT IGNORE INTO support_requests (id, user_id, created_at, subject, category, message, status, answer)
VALUES
(1, 1, CURDATE(), 'Нужен ранний заезд', 'Бронирование', 'Подскажите, можно ли приехать на два часа раньше?', 'В работе', 'Обращение получено, администратор уточняет наличие свободного номера.');
