<!DOCTYPE html>
<html>
<head>
    <title>Упражнение 4: Подключение к БД и вывод данных</title>
    <meta charset="UTF-8">
    <style>
        body { 
            font-family: Arial, sans-serif; 
            margin: 20px; 
            background-color: #f5f5f5;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h1, h2, h3 {
            color: #333;
        }
        .user-card {
            background: #f8f9fa;
            padding: 15px;
            margin: 10px 0;
            border-left: 3px solid #007bff;
            border-radius: 4px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Упражнение 4: Подключение к БД и вывод данных</h1>
        
        <?php
        $host = 'localhost';
        $username = 'root';
        $password = '';
        $dbname = 'SocialSite';

        try {
            $db = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8", $username, $password);
            $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            
            echo "<h2>Вывод данных из таблицы Users</h2>";
            $stmt = $db->query("SELECT id, nickname, login FROM Users");
            $users = $stmt->fetchAll(PDO::FETCH_ASSOC); 
            
            if (count($users) > 0) {
                foreach ($users as $user) {
                    echo "<div class='user-card'>";
                    echo "<strong>Номер пользователя {$user['id']}</strong><br>";
                    echo "Имя: {$user['nickname']}<br>";
                    echo "Логин: {$user['login']}";
                    echo "</div>";
                }
            } else {
                echo "<p >Ошибка</p>";
            }
        } catch(PDOException $e) {
            echo "<p class='error'>Ошибка: " . $e->getMessage() . "</p>";
        }
        ?>
    </div>
</body>
</html>