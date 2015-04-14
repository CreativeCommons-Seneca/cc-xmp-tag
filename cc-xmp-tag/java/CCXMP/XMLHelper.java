package CCXMP;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hosung on 07/04/15.
 */
public class XMLHelper {
    /**
     * This code block is from http://stackoverflow.com/questions/439298/best-way-to-encode-text-data-for-xml-in-java
     */
    public final static String ESCAPE_CHARS = "<>&\"\'";
    public final static List<String> ESCAPE_STRINGS = Collections.unmodifiableList(Arrays.asList(new String[]{
            "&lt;"
            , "&gt;"
            , "&amp;"
            , "&quot;"
            , "&apos;"
    }));

    private static String UNICODE_LOW =  "" + ((char)0x20); //space
    private static String UNICODE_HIGH = "" + ((char)0x7f);

    //should only use for the content of an attribute or tag
    public static String escapeXML(String content) {
        String result = content;

        if ((content != null) && (content.length() > 0)) {
            boolean modified = false;
            StringBuilder stringBuilder = new StringBuilder(content.length());
            for (int i = 0, count = content.length(); i < count; ++i) {
                String character = content.substring(i, i + 1);
                int pos = ESCAPE_CHARS.indexOf(character);
                if (pos > -1) {
                    stringBuilder.append(ESCAPE_STRINGS.get(pos));
                    modified = true;
                }
                else {
                    if ((character.compareTo(UNICODE_LOW) > -1) && (character.compareTo(UNICODE_HIGH) < 1)) {
                        stringBuilder.append(character);
                    }
                    else {
                        stringBuilder.append("&#" + ((int)character.charAt(0)) + ";");
                        modified = true;
                    }
                }
            }
            if (modified) {
                result = stringBuilder.toString();
            }
        }

        return result;
    }
}
