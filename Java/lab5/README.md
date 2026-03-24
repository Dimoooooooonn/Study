# Лабораторная работа №5

Проект доработан по шаблону MVC и собирается через Maven.

## Ключи запуска

```bash
java -jar target/garden-mvc-1.0.jar --help
java -jar target/garden-mvc-1.0.jar --show-once --file devices.txt
java -jar target/garden-mvc-1.0.jar --replace-file --file devices.txt --new-file devices_new.txt
java -jar target/garden-mvc-1.0.jar --watch --file devices.txt --period 5
java -jar target/garden-mvc-1.0.jar --csv-log --file devices.txt --csv changes.csv --period 5
java -jar target/garden-mvc-1.0.jar --interactive --file devices.txt
```

## Сборка

```bash
mvn clean package
```

## Структура

- `lab5.model` — модели устройств и работа с файлами
- `lab5.controller` — контроллеры
- `lab5.view` — консольное представление
- `devices.txt`, `devices_new.txt` — примеры входных файлов
