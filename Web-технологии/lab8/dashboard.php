<?php
session_start();
if (!isset($_SESSION['username'])) {
    header('Location: login.html');
    exit;
}

$username = htmlspecialchars($_SESSION['username']);
?>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Профиль</title>
</head>
<body>
<h2>Добро пожаловать, <?php echo $username; ?>!</h2>
<p>Это ваша личная страница.</p>
<p><a href="logout.php">Выйти</a></p>
</body>
</html>
