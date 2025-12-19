<?php
session_start();
require_once 'config.php';

$message = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = $_POST['username'] ?? '';
    $password_raw = $_POST['password'] ?? '';
    $email = $_POST['email'] ?? '';

    if (empty($username) || empty($password_raw) || empty($email)) {
        $message = "Пожалуйста, заполните все поля!";
    } else {
        $password_hash = password_hash($password_raw, PASSWORD_DEFAULT);

        try {
            $stmt = $db->prepare("INSERT INTO users (username, password, email) VALUES (?, ?, ?)");
            $stmt->execute([$username, $password_hash, $email]);
            $message = "Регистрация успешна! <a href='login.html'>Войти</a>";
        } catch (PDOException $e) {
            if ($e->getCode() == 23000) {
                $message = "Пользователь с таким логином или email уже существует!";
            } else {
                $message = "Ошибка: " . $e->getMessage();
            }
        }
    }
}
?>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Регистрация</title>
</head>
<body>
<h2>Результат регистрации</h2>
<p><?php echo $message; ?></p>
</body>
</html>
