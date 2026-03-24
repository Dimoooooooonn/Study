# Простой backend для сайта санатория на Java + MySQL

- регистрация
- вход и выход
- восстановление пароля
- создание бронирования
- формирование платежей после брони
- оплата из личного кабинета
- обновление статуса платежей и бронирования
- редактирование профиля
- отзывы
- обращения в поддержку

При первом запуске сервер:

1. читает настройки из `config/db.properties`
2. подключается к MySQL
3. создаёт базу `sanatorium_db`, если её ещё нет
4. создаёт таблицы
5. добавляет тестовые данные, если база пустая

## Какие таблицы создаются

- `users`
- `bookings`
- `payments`
- `reviews`
- `support_requests`

## Настройки подключения

Файл:

`config/db.properties`

Пример:

```properties
 db.host=localhost
 db.port=3306
 db.name=sanatorium_db
 db.user=root
 db.password=
```

## Тестовый аккаунт

- логин: `guest@example.com`
- пароль: `123456`

## Самый простой запуск

### Windows

```bat
start.bat
```

## Важные файлы

- `src/sanatorium/Main.java` — запуск
- `src/sanatorium/SanatoriumServer.java` — маршруты и логика сайта
- `src/sanatorium/DatabaseRepository.java` — обычный JDBC-код для MySQL
- `config/db.properties` — настройки подключения
- `db/sanatorium.sql` — SQL-скрипт базы
