<!DOCTYPE html>
<html>
<head>
    <title>Результат подсчета</title>
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
    $userText = $_POST['user_text'];

    $wordCount = str_word_count($userText, 0, "АаБбВвГгДдЕеЁёЖжЗзИиЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщЪъЫыЬьЭэЮюЯя");

    echo "<p><b>Исходная строка:</b> \"$userText\"</p>";
    echo "<p><b>Количество слов в строке:</b> $wordCount</p>";
    ?>
    <br>
    <a href="test.html">Вернуться к форме</a>
</body>
</html>
