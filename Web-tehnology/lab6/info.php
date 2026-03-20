<!DOCTYPE html>
<html>
<head>
    <title>Параметры сервера</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            line-height: 1.6;
            margin: 20px;
            background-color: #f5f5f5;
        }
        h1 {
            color: #2c3e50;
            text-align: center;
            padding-bottom: 10px;
        }
        p {
            background-color: white;
            padding: 12px;
            margin: 8px auto;
            border-radius: 5px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            width: 60%;
        }
        b {
            color: #e74c3c;
        }
    </style>
</head>
<body>
    <h1>Информация о сервере</h1>
    <p>1. IP-адрес удаленного компьютера: <b><?php echo $_SERVER['REMOTE_ADDR']; ?></b><br>
     Метод пересылки данных: <b><?php echo $_SERVER['REQUEST_METHOD']; ?></b></p>

    <p>2. Программное обеспечение сервера: <b><?php echo $_SERVER['SERVER_SOFTWARE']; ?></b><br>
    Протокол передачи данных: <b><?php echo $_SERVER['SERVER_PROTOCOL']; ?></b></p>

    <p>3. Каталог для хранения документов на сервере: <b><?php echo $_SERVER['DOCUMENT_ROOT']; ?></b><br>
      IP-адрес сервера: <b><?php echo $_SERVER['SERVER_ADDR']; ?></b></p>

    <p>4. Почтовый адрес администратора сети: <b><?php echo $_SERVER['SERVER_ADMIN'] ?? 'Не указан'; ?></b><br>
      Имя хост-компьютера: <b><?php echo $_SERVER['HTTP_HOST']; ?></b></p>
</body>
</html>
