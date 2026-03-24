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
        } catch (BindException e) {
            System.out.println("Порт " + port + " уже занят.");
            System.out.println("Попробуйте запустить так: java -cp out sanatorium.Main 9090");
            return;
        }

        System.out.println("Папка проекта: " + projectDir);
        System.out.println("Тестовый аккаунт: guest@example.com");
        System.out.println("Пароль: 123456");

        while (true) {
            Thread.sleep(60_000);
        }
    }
}
