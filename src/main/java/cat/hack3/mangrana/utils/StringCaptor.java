package cat.hack3.mangrana.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCaptor {

    public static String getMatchingSubstring(String fullText, String matchingRegex) {
        Pattern pattern = Pattern.compile(matchingRegex);
        Matcher matcher = pattern.matcher(fullText);
        return matcher.find() ? matcher.group(1) : null;
    }

}
