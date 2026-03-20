<!DOCTYPE html>
<html>
<head>
    <title>Результат расчета</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            line-height: 1.6;
            margin: 20px;
            background-color: #f5f5f5;
        }
    </style>
</head>
<body>
    <h2>Результат</h2>
    <?php

    $surname = $_POST['surname'];
    $grade1 = (int)$_POST['grade1'];
    $grade2 = (int)$_POST['grade2'];
    $grade3 = (int)$_POST['grade3'];

    $average = ($grade1 + $grade2 + $grade3) / 3;

    echo "<p><b>Фамилия:</b> $surname</p>";
    echo "<p><b>Оценки:</b> $grade1, $grade2, $grade3</p>";
    echo "<p><b>Средний балл:</b> " . number_format($average, 2) . "</p>";

    if ($average >= 4 && $grade1 >= 4 && $grade2 >= 4 && $grade3 >= 4) {
        echo "<p style='color:green;'><b>Решение: Назначить стипендию.</b></p>";
    } else {
        echo "<p style='color:red;'><b>Решение: Стипендия не назначена.</b></p>";
    }
    ?>
    <br>
    <a href="form.html">Вернуться к форме</a>
</body>
</html>
