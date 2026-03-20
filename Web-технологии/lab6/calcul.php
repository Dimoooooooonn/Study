<!DOCTYPE html>
<html>
<head>
    <title>Сложение</title>
</head>
<body>
    <?php
    // Получаем значения параметров var1 и var2 из URL
    $a = $_GET['var1'];
    $b = $_GET['var2'];

    // Проверяем, что параметры являются числами
    if (is_numeric($a) && is_numeric($b)) {
        $sum = $a + $b;
        echo "Сумма чисел $a и $b равна: $sum";
    } else {
        echo "Ошибка: Оба параметра должны быть числами!";
    }
    ?>
</body>
</html>
