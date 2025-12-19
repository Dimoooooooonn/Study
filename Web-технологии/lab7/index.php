<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Анкета</title>
</head>
<body>


<?php

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $host = 'localhost';
    $username = 'root';
    $password = '';
    $dbname = 'users_lab1';
    
    try {
        $db = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8", $username, $password);
        $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        
     
        $login = $_POST['login'] ?? '';
        $password_form = $_POST['password'] ?? '';
        $city = $_POST['city'] ?? '';
        $email = $_POST['mail'] ?? '';
        $age = $_POST['age'] ?? '';
        
    
$hobbies = '';
if (!empty($_POST['hobby'])) {
    $hobbiesArray = (array) $_POST['hobby'];
    $hobbies = implode(', ', $hobbiesArray);
}

        $errors = [];
                if (strlen($login) < 3) {
            $errors[] = "Логин должен содержать не менее 3 символов";
        } elseif (strlen($login) > 15) {
            $errors[] = "Логин должен содержать не более 15 символов";
        }

if (strlen($password_form) < 10) {
            $errors[] = "Пароль должен содержать не менее 10 символов";
        } elseif (strlen($password_form) > 15) {
            $errors[] = "Пароль должен содержать не более 15 символов";
        }
        if (!empty($errors)) {
            $message = "<div class='message error'><strong>Ошибки валидации:</strong><br> " . implode('<br> ', $errors) . "</div>";
        } else {
            $hashed_password = password_hash($password_form, PASSWORD_DEFAULT);
            $stmt = $db->prepare("INSERT INTO users (login, password, country, mail, age, hobby) VALUES (?, ?, ?, ?, ?, ?)");
            $stmt->execute([$login, $hashed_password, $city, $email, $age, $hobbies]);
            
            $message = "<div class='message success'>Данные успешно сохранены в базу данных!</div>";
        }
    } catch(PDOException $e) {
        $message = "<div class='message error'>Ошибка базы данных: " . $e->getMessage() . "</div>";
    }
}
?>


<?php
if (!empty($message)) {
    echo $message;
}
?>

        <form method="POST">
    <h3> Анкета пользователя</h3>
    <table>
    <tr>
    <td> Логин: </td>
    <td> <input type="text" name="login" size="30"></td>
    <td> Пароль: </td>
    <td> <input type="password" name="password" size="10"></td></tr>
    <tr>
    <td> Город: </td>
    <td> <select name ="city">
    <option value="Moscow"> Москва
    <option selected=2 value="SPB"> Санкт-Петербург
    <option value="Kazan"> Казань
    <option value="Myrman"> Мурманск
    <option value="Other"> Другой...
    </select> </td></tr>
    <tr>
    <td> Почта: </td>
    <td> <input type="email" name="mail" size="30"></td>
    </tr>
    </table>
    <h4> Укажите свою возрастную группу </h4>
    <input type="radio" name="age" value="child"> 7-12 лет
    <input type="radio" name="age" value="junior" checked> 13-20 лет
    <input type="radio" name="age" value="adult"> от 20 лет
    <h4> Укажите свои увлечения </h4>
    <input type="checkbox" name="hobby[]" value="computers"> Компьютеры
    <input type="checkbox" name="hobby[]" value="art"> Литература
    <input type="checkbox" name="hobby[]" value="music"> Музыка
    <input type="checkbox" name="hobby[]" value="avto"> Автомобили
    <input type="checkbox" name="hobby[]" value="sport"> Спорт
    <br>
    <input type="submit" value="Отправить">
    <input type="reset" value="Очистить">
    </form>
</body>
</html>