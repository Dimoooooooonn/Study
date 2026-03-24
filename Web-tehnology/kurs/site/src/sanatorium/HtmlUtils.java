package sanatorium;

public class HtmlUtils {
    public static String escape(String text) {
        if (text == null) {
            return "";
        }
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    public static String messageBlock(String message, boolean ok) {
        if (message == null || message.isBlank()) {
            return "";
        }
        String cls = ok ? "notice notice--ok" : "notice notice--warn";
        return "<div class=\"" + cls + "\" style=\"margin-bottom:14px\"><div>" + escape(message) + "</div></div>";
    }

    public static String nl2br(String text) {
        return escape(text).replace("\n", "<br>");
    }

    public static String stars(int rating) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < rating; i++) {
            builder.append("★");
        }
        for (int i = rating; i < 5; i++) {
            builder.append("☆");
        }
        return builder.toString();
    }
}
