package sanatorium;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateUtils {
    private static final Pattern PLACEHOLDER = Pattern.compile("\\{\\{([a-zA-Z0-9]+)}}");

    public static String render(String template, Map<String, String> values) {
        Matcher matcher = PLACEHOLDER.matcher(template);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = values.getOrDefault(key, "");
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(value));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }
}
