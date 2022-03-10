package base;

public class StringHelper {
    public static boolean containsIgnoreCase(String source, String target) {
        final int SOURCE_LENGTH = source.length();
        final int TARGET_LENGTH = target.length();
        if (TARGET_LENGTH == 0) {
            return true;
        }

        final char FIRST_CHAR_LOWER = Character.toLowerCase(target.charAt(0));
        final char FIRST_CHAR_UPPER = Character.toUpperCase(target.charAt(0));

        for (int i = SOURCE_LENGTH - TARGET_LENGTH; i >= 0; --i) {
            // Quick check before calling the more expensive regionMatches() method:
            char ch = source.charAt(i);
            if (ch != FIRST_CHAR_LOWER && ch != FIRST_CHAR_UPPER) {
                continue;
            }

            if (source.regionMatches(true, i, target, 0, TARGET_LENGTH)) {
                return true;
            }
        }

        return false;
    }
}
