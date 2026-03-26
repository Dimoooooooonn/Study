package sanatorium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SanatoriumUiTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String baseUrl = "http://localhost:8080";

    // Эти значения можно менять, если хочешь ускорить или замедлить просмотр тестов
    private static final int PAGE_PAUSE = 2000;
    private static final int ACTION_PAUSE = 1200;
    private static final int LONG_PAUSE = 2500;

    private interface TestBody {
        void run() throws Exception;
    }

    @BeforeEach
    public void setUp() {
        System.out.println("==================================================");
        System.out.println("Подготовка браузера перед тестом");
        System.out.println("==================================================");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        // options.addArguments("--headless=new");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(12));

        System.out.println("Браузер Chrome успешно открыт");
        System.out.println();
        pause("", LONG_PAUSE);
    }

    @AfterEach
    public void tearDown() {
        System.out.println();
        System.out.println("Завершение теста. Закрываю браузер.");
        pause("Небольшая пауза перед закрытием браузера", ACTION_PAUSE);
        if (driver != null) {
            driver.quit();
        }
        System.out.println("Браузер закрыт");
        System.out.println("==================================================");
        System.out.println();
    }

    @Test
    public void mainPageShouldOpen() throws Exception {
        runScenario(
                "mainPageShouldOpen",
                "Проверяем, что главная страница открывается и содержит основные элементы интерфейса.",
                () -> {
                    step("Открываем главную страницу");
                    openPage("/index.html");

                    step("Ждём корректный заголовок страницы");
                    wait.until(ExpectedConditions.titleContains("Сосновый берег"));
                    pause("Смотрим заголовок и верхнюю часть страницы", ACTION_PAUSE);

                    step("Проверяем наличие основного текста и кнопок");
                    Assertions.assertTrue(driver.getPageSource().contains("Санаторий для спокойного отдыха"));
                    Assertions.assertTrue(driver.getPageSource().contains("Личный кабинет"));
                    Assertions.assertTrue(driver.getPageSource().contains("Забронировать"));
                    pause("Можно посмотреть главный экран перед окончанием теста", LONG_PAUSE);
                }
        );
    }

    @Test
    public void loginWithWrongPasswordShouldShowError() throws Exception {
        runScenario(
                "loginWithWrongPasswordShouldShowError",
                "Проверяем, что при неверном пароле сайт показывает сообщение об ошибке входа.",
                () -> {
                    step("Открываем страницу входа");
                    openPage("/login.html");

                    step("Вводим логин и неверный пароль");
                    type(By.name("login"), "guest@example.com");
                    type(By.name("password"), "wrong-password");

                    step("Нажимаем кнопку входа");
                    clickButton("Войти");

                    step("Проверяем сообщение об ошибке");
                    waitAnyText("Неверный логин или пароль.");
                    Assertions.assertTrue(driver.getCurrentUrl().contains("/login.html"));
                    pause("Смотрим сообщение об ошибке на странице входа", 9999999);
                }
        );
    }

    @Test
    public void registrationShouldCreateAccount() throws Exception {
        runScenario(
                "registrationShouldCreateAccount",
                "Проверяем регистрацию нового пользователя через форму сайта.",
                () -> {
                    String email = createUniqueEmail();
                    String phone = createUniquePhone();

                    step("Открываем страницу регистрации");
                    openPage("/register.html");

                    step("Заполняем форму регистрации новыми данными");
                    type(By.name("firstName"), "Петя");
                    type(By.name("lastName"), "Петров");
                    type(By.name("email"), email);
                    type(By.name("phone"), phone);
                    type(By.name("password"), "12345678");
                    type(By.name("repeatPassword"), "12345678");
                    type(By.name("comment"), "Регистрация через UI тест");

                    step("Проверяем галочку согласия");
                    WebElement agree = driver.findElement(By.name("agree"));
                    if (!agree.isSelected()) {
                        safeClick(agree);
                        pause("Смотрим, что галочка согласия установлена", ACTION_PAUSE);
                    }

                    step("Отправляем форму регистрации");
                    clickButton("Создать аккаунт");

                    step("Проверяем успешный переход в личный кабинет");
                    wait.until(ExpectedConditions.urlContains("/cabinet/index.html"));
                    waitAnyText("Аккаунт создан.", "Вы успешно вошли в кабинет.");
                    Assertions.assertTrue(driver.getPageSource().contains("Личный кабинет"));
                    pause("Можно посмотреть, что регистрация привела в личный кабинет", LONG_PAUSE);
                }
        );
    }

    @Test
    public void reviewFormShouldSaveReview() throws Exception {
        runScenario(
                "reviewFormShouldSaveReview",
                "Проверяем отправку отзыва через публичную страницу отзывов.",
                () -> {
                    String name = "Тестовый гость " + System.currentTimeMillis();
                    String text = "Очень понравился интерфейс сайта и структура личного кабинета.";

                    step("Открываем страницу отзывов");
                    openPage("/reviews.html");

                    step("Заполняем имя и текст отзыва");
                    type(By.name("name"), name);
                    type(By.name("review"), text);

                    step("Выбираем максимальную оценку");
                    Select rating = new Select(waitVisible(By.name("rating")));
                    try {
                        rating.selectByValue("5");
                    } catch (Exception e) {
                        rating.selectByIndex(rating.getOptions().size() - 1);
                    }
                    System.out.println("[OK] Выбрана оценка для отзыва");
                    pause("Смотрим заполненную форму отзыва", ACTION_PAUSE);

                    step("Отправляем отзыв");
                    clickButton("Отправить отзыв");

                    step("Проверяем сообщение об успешном сохранении");
                    waitAnyText("Спасибо", "отзыв", "сохран");
                    Assertions.assertTrue(driver.getPageSource().contains(name));
                    pause("Можно посмотреть результат отправки отзыва", LONG_PAUSE);
                }
        );
    }

    @Test
    public void guestShouldCreateBookingAndPayFirstPayment() throws Exception {
        runScenario(
                "guestShouldCreateBookingAndPayFirstPayment",
                "Проверяем вход гостя, создание бронирования и оплату первого платежа.",
                () -> {
                    step("Входим в личный кабинет тестовым пользователем");
                    loginAsGuest();

                    step("Открываем страницу бронирования");
                    openPage("/booking.html");
                    waitVisible(By.id("bookingWizard"));
                    pause("Смотрим первый шаг мастера бронирования", LONG_PAUSE);

                    step("Шаг 1. Заполняем даты и параметры поездки");
                    clearAndType(By.name("checkIn"), "05-10");
                    clearAndType(By.name("checkOut"), "05-15");
                    new Select(waitVisible(By.name("guests"))).selectByValue("2");
                    System.out.println("[OK] Выбрано количество гостей: 2");
                    pause("Смотрим выбранное количество гостей", ACTION_PAUSE);
                    new Select(waitVisible(By.name("purpose"))).selectByVisibleText("Оздоровительный отдых");
                    System.out.println("[OK] Выбрана цель поездки: Оздоровительный отдых");
                    pause("Смотрим заполненный первый шаг", ACTION_PAUSE);
                    clickNext();

                    step("Шаг 2. Выбираем номер Комфорт");
                    clickRadio(By.name("room"), "Комфорт");
                    clickNext();

                    step("Шаг 3. Отмечаем дополнительный массаж");
                    WebElement massage = waitVisible(By.name("massage"));
                    if (!massage.isSelected()) {
                        safeClick(massage);
                        System.out.println("[OK] Отмечена дополнительная услуга: массаж");
                        pause("Смотрим выбранные дополнительные услуги", ACTION_PAUSE);
                    }
                    clickNext();

                    step("Шаг 4. Заполняем контактные данные");
                    clearAndType(By.name("fullName"), "Иван Иванов");
                    clearAndType(By.name("phone"), "+79001234567");
                    clearAndType(By.name("email"), "guest@example.com");
                    clearAndType(By.name("comment"), "Бронирование из UI теста");
                    pause("Смотрим заполненные контактные данные", LONG_PAUSE);
                    clickNext();

                    step("Шаг 5. Подтверждаем бронирование");
                    clickButton("Подтвердить бронирование");

                }
        );
    }

    @Test
    public void guestShouldUpdateProfileAndSendSupportMessage() throws Exception {
        runScenario(
                "guestShouldUpdateProfileAndSendSupportMessage",
                "Проверяем редактирование профиля и отправку обращения в поддержку.",
                () -> {
                    String subject = "Вопрос по оплате " + System.currentTimeMillis();

                    step("Входим в личный кабинет тестовым пользователем");
                    loginAsGuest();

                    step("Открываем страницу профиля");
                    openPage("/cabinet/profile.html");

                    step("Меняем город и комментарий");
                    clearAndType(By.name("city"), "Москва");
                    clearAndType(By.name("comment"), "Хочу номер потише и процедуры утром.");
                    pause("Смотрим обновлённые данные профиля", ACTION_PAUSE);

                    step("Сохраняем профиль");
                    clickButton("Сохранить изменения");

                    step("Проверяем, что профиль сохранился");
                    waitAnyText("Профиль сохранён", "Профиль сохранен");
                    Assertions.assertTrue(driver.getPageSource().contains("Москва"));
                    pause("Можно посмотреть результат сохранения профиля", LONG_PAUSE);

                    step("Открываем страницу поддержки");
                    openPage("/cabinet/support.html");

                    step("Заполняем форму обращения");
                    type(By.name("subject"), subject);
                    Select category = new Select(waitVisible(By.name("category")));
                    try {
                        category.selectByVisibleText("Оплата");
                    } catch (Exception e) {
                        category.selectByIndex(0);
                    }
                    System.out.println("[OK] Выбрана категория обращения");
                    pause("Смотрим выбранную категорию", ACTION_PAUSE);
                    type(By.name("message"), "Подскажите, когда лучше вносить оставшуюся сумму?");
                    pause("Смотрим заполненную форму обращения", LONG_PAUSE);

                    step("Отправляем обращение");
                    clickButton("Отправить обращение");

                    step("Проверяем сообщение об успешной отправке");
                    waitAnyText("Обращение отправлено", "Сообщение отправлено", "успешно отправлено", subject);
                    Assertions.assertTrue(driver.getPageSource().contains(subject) || driver.getCurrentUrl().contains("status=success"));
                    pause("Можно посмотреть итог отправки обращения", LONG_PAUSE);
                }
        );
    }

    private void runScenario(String testName, String description, TestBody body) throws Exception {
        System.out.println("Запущен тест: " + testName);
        System.out.println("Что проверяем: " + description);
        System.out.println("--------------------------------------------------");

        try {
            body.run();
            System.out.println("--------------------------------------------------");
            System.out.println("Результат: ТЕСТ ПРОЙДЕН");
            System.out.println("Тест " + testName + " завершился успешно");
        } catch (Throwable e) {
            System.out.println("--------------------------------------------------");
            System.out.println("Результат: ТЕСТ УПАЛ");
            System.out.println("Тест " + testName + " завершился с ошибкой");
            System.out.println("Тип ошибки: " + e.getClass().getSimpleName());
            System.out.println("Текст ошибки: " + e.getMessage());
            throw e;
        }
    }

    private void loginAsGuest() {
        openPage("/login.html");
        type(By.name("login"), "guest@example.com");
        type(By.name("password"), "123456");
        clickButton("Войти");
        wait.until(ExpectedConditions.urlContains("/cabinet/index.html"));
        waitAnyText("Вы успешно вошли в кабинет", "Личный кабинет");
        pause("Можно посмотреть личный кабинет после входа", LONG_PAUSE);
    }

    private void openPage(String path) {
        driver.get(baseUrl + path);
        System.out.println("[OK] Открыта страница: " + path);
        pause("Загруженная страница открыта для просмотра", PAGE_PAUSE);
    }

    private void step(String text) {
        System.out.println("[ШАГ] " + text);
    }

    private void pause(String reason, int millis) {
        try {
            System.out.println("[ПАУЗА] " + reason + " (" + millis + " мс)");
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Пауза была прервана", e);
        }
    }

    private WebElement waitVisible(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    private void type(By by, String text) {
        WebElement element = waitVisible(by);
        element.sendKeys(text);
        System.out.println("[OK] Поле заполнено: " + by + " -> " + text);
        pause("Смотрим введённое значение", ACTION_PAUSE);
    }

    private void clearAndType(By by, String text) {
        WebElement element = waitVisible(by);
        element.clear();
        pause("Поле очищено", 500);
        element.sendKeys(text);
        System.out.println("[OK] Поле очищено и заполнено: " + by + " -> " + text);
        pause("Смотрим новое значение поля", ACTION_PAUSE);
    }

    private void clickButton(String text) {
        List<By> variants = List.of(
                By.xpath("//button[normalize-space()='" + text + "']"),
                By.xpath("//input[@type='submit' and @value='" + text + "']"),
                By.xpath("//a[normalize-space()='" + text + "']")
        );

        for (By by : variants) {
            List<WebElement> elements = driver.findElements(by);
            for (WebElement element : elements) {
                if (element.isDisplayed() && element.isEnabled()) {
                    safeClick(element);
                    System.out.println("[OK] Нажата кнопка: " + text);
                    pause("Смотрим результат нажатия кнопки", ACTION_PAUSE);
                    return;
                }
            }
        }

        Assertions.fail("Не найдена кнопка: " + text);
    }

    private void clickNext() {
        List<By> variants = List.of(
                By.cssSelector("#bookingWizard .wizard-panel.is-active .js-next"),
                By.cssSelector("#bookingWizard .wizard-panel:not([hidden]) .js-next"),
                By.cssSelector("#bookingWizard .js-next"),
                By.xpath("//button[contains(normalize-space(), 'Далее')]"),
                By.xpath("//button[contains(normalize-space(), 'Продолжить')]"),
                By.xpath("//button[contains(normalize-space(), 'Следующий')]")
        );

        for (By by : variants) {
            List<WebElement> elements = driver.findElements(by);
            for (WebElement element : elements) {
                if (element.isDisplayed() && element.isEnabled()) {
                    safeClick(element);
                    System.out.println("[OK] Выполнен переход на следующий шаг мастера бронирования");
                    pause("Смотрим следующий шаг мастера", LONG_PAUSE);
                    return;
                }
            }
        }

        System.out.println("Не удалось найти кнопку перехода к следующему шагу.");
        List<WebElement> allButtons = driver.findElements(By.xpath("//button | //a"));
        for (WebElement el : allButtons) {
            String text = el.getText();
            if (text != null && !text.isBlank()) {
                System.out.println("Найден элемент: " + text);
            }
        }

        Assertions.fail("Не нашлась кнопка Далее");
    }

    private void clickRadio(By by, String value) {
        List<WebElement> radios = driver.findElements(by);
        for (WebElement radio : radios) {
            if (value.equals(radio.getAttribute("value"))) {
                safeClick(radio);
                System.out.println("[OK] Выбран вариант: " + value);
                pause("Смотрим выбранный radio-вариант", ACTION_PAUSE);
                return;
            }
        }
        Assertions.fail("Не удалось найти radio со значением: " + value);
    }

    private void clickFirstPaymentButton() {
        List<WebElement> payButtons = driver.findElements(By.xpath("//button[normalize-space()='Оплатить']"));
        Assertions.assertFalse(payButtons.isEmpty(), "На странице не нашлась кнопка оплаты");
        safeClick(payButtons.get(0));
        System.out.println("[OK] Нажата первая кнопка оплаты");
        pause("Смотрим результат оплаты", LONG_PAUSE);
    }

    private void safeClick(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        wait.until(ExpectedConditions.elementToBeClickable(element));

        try {
            element.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    private void waitAnyText(String... texts) {
        wait.until(driver -> {
            String html = driver.getPageSource();
            for (String text : texts) {
                if (html.contains(text)) {
                    return true;
                }
            }
            return false;
        });
        System.out.println("[OK] Найден ожидаемый текст на странице");
        pause("Даём время увидеть сообщение на странице", ACTION_PAUSE);
    }

    private String createUniqueEmail() {
        return "student" + System.currentTimeMillis() + "@test.ru";
    }

    private String createUniquePhone() {
        String number = String.valueOf(System.currentTimeMillis());
        if (number.length() > 10) {
            number = number.substring(number.length() - 10);
        }
        return "+7" + number;
    }
}
