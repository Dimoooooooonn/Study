# Лабораторная работа №6
## Сетевые технологии

Проект разделён на 3 Maven-модуля:
- `garden-common` — общие DTO, сетевой протокол, загрузка `application.properties`
- `garden-server` — серверная часть, хранение данных, логика устройств
- `garden-client` — клиентская часть, консольный интерфейс, ключи запуска, CSV-логирование

## Что выполнено по заданию

### Серверная часть
Сервер обеспечивает:
- хранение данных объектов в текстовом формате из ЛР4;
- обработку запросов клиента;
- выдачу данных объектов по запросу;
- моделирование логики функционала классов на сервере.

### Клиентская часть
Клиент обеспечивает:
- получение информации об объектах с сервера;
- отображение информации в консоли;
- отсутствие постоянного хранения объектов на клиенте;
- чтение IP-адреса и порта из `application.properties`.

### Сетевой стек
Используется только TCP.
Протокол верхнего уровня не используется.
Реализован собственный бинарный протокол поверх TCP.

## Структура сетевого пакета
Каждое сообщение режется на кадры длиной до `1400` байт.
Это сделано с учётом стандартного MTU Ethernet `1500` байт и заголовков IP/TCP.

Заголовок кадра занимает 16 байт:
- `magic` — 2 байта
- `version` — 1 байт
- `flags` — 1 байт
- `requestId` — 4 байта
- `totalPayloadLength` — 4 байта
- `chunkLength` — 4 байта

Остальная часть кадра — полезная нагрузка.
Если сообщение большое, оно разбивается на несколько кадров.

## Формат данных сервера
Сервер хранит объекты в файлах `garden-server/server_data/devices.txt` и `devices_new.txt`.
Формат совместим с ЛР4:

```text
Type: LavnMover,
device_manufacturer: Bosch,
device_model: ARM37,
power_supply: battery,
powered_on: true,
work_intensity: 2,
wear_level: 12,
grass_container_level: 30,
blade_sharpness: 88,
cut_height: 5,
```

Лишние поля игнорируются.
Если обязательного поля нет, объект всё равно создаётся, а отсутствующее свойство учитывается в статистике.

## Команды клиента
- `--help`
- `--show-once`
- `--replace-file --new-file <имя_файла>`
- `--watch --period <секунды>`
- `--csv-log --period <секунды> --csv <имя_csv>`
- `--interactive`

## Примеры запуска

### 1. Сборка всего проекта
```bash
mvn clean package
```

### 2. Запуск сервера
```bash
java -jar garden-server/target/garden-server-1.0.jar
```

### 3. Разовый вывод информации с клиента
```bash
java -jar garden-client/target/garden-client-1.0.jar --show-once
```

### 4. Замена файла на сервере
```bash
java -jar garden-client/target/garden-client-1.0.jar --replace-file --new-file devices_new.txt
```

### 5. Цикличный вывод
```bash
java -jar garden-client/target/garden-client-1.0.jar --watch --period 5
```

### 6. CSV логирование
```bash
java -jar garden-client/target/garden-client-1.0.jar --csv-log --period 5 --csv changes.csv
```

### 7. Интерактивный режим
```bash
java -jar garden-client/target/garden-client-1.0.jar --interactive
```
