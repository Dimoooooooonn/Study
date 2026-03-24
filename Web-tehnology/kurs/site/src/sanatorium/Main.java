package sanatorium;

import java.net.BindException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception ignored) {
            }
        }

        Path projectDir = Path.of("").toAbsolutePath().normalize();

        try {
            SanatoriumServer server = new SanatoriumServer(port, projectDir);
            server.start();
            System.out.println("Сервер запущен: http://localhost:" + port);
            System.out.println("Папка проекта: " + projectDir);
            System.out.println("Тестовый аккаунт: guest@example.com");
            System.out.println("Пароль: 123456");
            System.out.println("Подключение к БД берётся из файла: config/db.properties");
        } catch (BindException e) {
            System.out.println("Порт " + port + " уже занят.");
            System.out.println("Попробуй запустить так: start.bat 9090");
            return;
        } catch (Exception e) {
            System.out.println("Не удалось запустить сервер.");
            System.out.println(e.getMessage());
            return;
        }

        while (true) {
            Thread.sleep(60_000);
        }
    }
}
