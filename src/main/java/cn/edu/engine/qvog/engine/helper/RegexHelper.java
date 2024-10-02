package cn.edu.engine.qvog.engine.helper;

import java.util.regex.Pattern;

public class RegexHelper {
    private RegexHelper() {}

    public static Pattern wildcardToRegex(String wildcard) {
        wildcard = wildcard.replace(".", "\\.");
        wildcard = wildcard.replace("*", ".*");
        wildcard = '^' + wildcard + '$';
        return Pattern.compile(wildcard, Pattern.CASE_INSENSITIVE);
    }

    public static boolean match(Pattern pattern, String text) {
        return pattern.matcher(text).find();
    }
}
