package sanatorium;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class PasswordUtils {
    public static String hash(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte b : bytes) {
                String part = Integer.toHexString(0xff & b);
                if (part.length() == 1) {
                    builder.append('0');
                }
                builder.append(part);
            }
            return builder.toString();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось захешировать пароль", e);
        }
    }
}
