package main.java;

// REF:https://stackoverflow.com/questions/86780/how-to-check-if-a-string-contains-another-string-in-a-case-insensitive-manner-in/25379180#25379180
public class StringHelper {
    public static boolean containsIgnoreCase(String source, String target) {
        final int SOURCE_LENGTH = source.length();
        final int TARGET_LENGTH = target.length();
        if (TARGET_LENGTH == 0) {
            return true;
        }

        for (int i = SOURCE_LENGTH - TARGET_LENGTH; i >= 0; --i) {
            // Quick check before calling the more expensive regionMatches() method:
            final char FIRST_TARGET_CHAR_LOWER = Character.toLowerCase(target.charAt(0));
            final char FIRST_TARGET_CHAR_UPPER = Character.toUpperCase(target.charAt(0));
            final char ch = source.charAt(i);
            if (ch != FIRST_TARGET_CHAR_LOWER && ch != FIRST_TARGET_CHAR_UPPER) {
                continue;
            }

            // Tests if two string regions are equal
            if (source.regionMatches(true, i, target, 0, TARGET_LENGTH)) {
                return true;
            }
        }

        return false;
    }

    public static int numIgnoreCase(String source, String target) {
        final int SOURCE_LENGTH = source.length();
        final int TARGET_LENGTH = target.length();
        if (TARGET_LENGTH == 0) {
            return 0;
        }

        int count = 0;
        for (int i = SOURCE_LENGTH - TARGET_LENGTH; i >= 0; --i) {
            // Quick check before calling the more expensive regionMatches() method:
            final char FIRST_TARGET_CHAR_LOWER = Character.toLowerCase(target.charAt(0));
            final char FIRST_TARGET_CHAR_UPPER = Character.toUpperCase(target.charAt(0));
            final char ch = source.charAt(i);
            if (ch != FIRST_TARGET_CHAR_LOWER && ch != FIRST_TARGET_CHAR_UPPER) {
                continue;
            }

            // Tests if two string regions are equal
            if (source.regionMatches(true, i, target, 0, TARGET_LENGTH)) {
                count++;
            }
        }

        return count;
    }
}
