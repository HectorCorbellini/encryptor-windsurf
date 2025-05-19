package domain.encryption;

/**
 * Configuration class for encryption operations.
 * This class centralizes all configuration values used in the encryption process,
 * making it easier to modify them in a single place.
 */
public class EncryptionConfig {
    // ASCII character range constants for text processing
    public static final int CHAR_START = 32;      // First processable character (Space)
    public static final int CHAR_END = 255;       // Last processable character (Extended ASCII)
    public static final int CHAR_RANGE = CHAR_END - CHAR_START + 1;  // Total characters we can process
    
    // ASCII values for text analysis and pattern detection
    public static final int UPPERCASE_A = 65;     // Start of uppercase alphabet range
    public static final int UPPERCASE_Z = 90;     // End of uppercase alphabet range
    public static final int LOWERCASE_A = 97;     // Start of lowercase alphabet range
    public static final int LOWERCASE_Z = 122;    // End of lowercase alphabet range
    public static final int COMMA = 44;           // Punctuation for sentence structure
    public static final int PERIOD = 46;          // Sentence terminator (used in pattern detection)
    public static final int LINE_FEED = 10;       // Line ending '\n' (used in pattern detection)
    public static final int CARRIAGE_RETURN = 13; // Line ending '\r' (Windows-style)
    
    // Pattern detection configuration
    public static final int PATTERN_DISTANCE = 31; // Common byte distance between '.' and '\n' in text
    public static final double MIN_READABILITY_SCORE = 0.7; // Text needs 70% readable chars to be valid
    
    // File paths for default files
    public static final String DEFAULT_SOURCE_FILE = "test/textoPrueba.txt";
    public static final String DEFAULT_ENCRYPTED_FILE = "test/textoEncrip.txt";
    public static final String DEFAULT_DECRYPTED_FILE = "test/textoDecrypted.txt";
    
    // UI configuration
    public static final int PREVIEW_LENGTH = 150; // Number of characters to show in preview
    
    /**
     * Checks if a character is in the specified range
     * 
     * @param c the character to check
     * @param start the start of the range (inclusive)
     * @param end the end of the range (inclusive)
     * @return true if the character is in the range, false otherwise
     */
    public static boolean isInRange(int c, int start, int end) {
        return c >= start && c <= end;
    }
    
    /**
     * Checks if a character is a common readable character
     * 
     * @param c the character to check
     * @return true if the character is readable, false otherwise
     */
    public static boolean isReadable(int c) {
        return isInRange(c, LOWERCASE_A, LOWERCASE_Z) ||
               isInRange(c, UPPERCASE_A, UPPERCASE_Z) ||
               c == CHAR_START || // Space
               c == PERIOD ||
               c == COMMA ||
               c == LINE_FEED ||
               c == CARRIAGE_RETURN;
    }
}
