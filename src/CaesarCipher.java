import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the Caesar Cipher encryption algorithm.
 * This class handles the core encryption and decryption logic.
 */
public class CaesarCipher {
    // ASCII character range for encryption/decryption
    private static final int CHAR_START = 32;      // Space character
    private static final int CHAR_END = 255;       // Extended ASCII end
    private static final int CHAR_RANGE = CHAR_END - CHAR_START + 1;
    
    // Common ASCII characters for text analysis
    private static final int UPPERCASE_A = 65;     // 'A'
    private static final int UPPERCASE_Z = 90;     // 'Z'
    private static final int LOWERCASE_A = 97;     // 'a'
    private static final int LOWERCASE_Z = 122;    // 'z'
    private static final int COMMA = 44;           // ','
    private static final int PERIOD = 46;          // '.'
    private static final int LINE_FEED = 10;       // '\n'
    private static final int CARRIAGE_RETURN = 13; // '\r'
    
    // Pattern detection constants
    private static final int PATTERN_DISTANCE = 31; // Expected distance between period and line feed
    private static final double MIN_READABILITY_SCORE = 0.7; // Minimum ratio of readable characters

    /**
     * Encrypts a file using Caesar cipher with the given key
     * 
     * @param sourceFile the file to encrypt
     * @param destinationFile the file to write encrypted content to
     * @param key the encryption key (how many positions to shift each character)
     * @throws IOException if an I/O error occurs
     * @throws IllegalArgumentException if the key is negative
     */
    public void encryptFile(String sourceFile, String destinationFile, int key) throws IOException {
        if (key < 0) {
            throw new IllegalArgumentException("Encryption key must be positive");
        }
        processFile(sourceFile, destinationFile, key);
    }
    
    /**
     * Decrypts a file using Caesar cipher with the given key
     * 
     * @param sourceFile the file to decrypt
     * @param destinationFile the file to write decrypted content to
     * @param key the decryption key (should be negative)
     * @throws IOException if an I/O error occurs
     * @throws IllegalArgumentException if the key is positive
     */
    public void decryptFile(String sourceFile, String destinationFile, int key) throws IOException {
        if (key > 0) {
            throw new IllegalArgumentException("Decryption key must be negative");
        }
        processFile(sourceFile, destinationFile, key);
    }
    
    /**
     * Processes a file by shifting each character by the key value
     * 
     * @param sourceFile the input file
     * @param destinationFile the output file
     * @param key the shift key (positive for encryption, negative for decryption)
     * @throws IOException if an I/O error occurs
     */
    private void processFile(String sourceFile, String destinationFile, int key) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile), StandardCharsets.UTF_8));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destinationFile), StandardCharsets.UTF_8))) {
            
            StringBuilder buffer = new StringBuilder();
            int character;
            
            while ((character = reader.read()) != -1) {
                char ch = (char) character;
                buffer.append(ch);
                
                // Process buffer when we have enough characters
                if (buffer.length() >= 3) {
                    // Check for special sequences
                    if (buffer.toString().endsWith("...")) {
                        // Write all characters before the ellipsis
                        String beforeEllipsis = buffer.substring(0, buffer.length() - 3);
                        processChars(beforeEllipsis, writer, key);
                        
                        // Write ellipsis as-is
                        writer.write("...");
                        buffer.setLength(0);
                    } else {
                        // Process first character
                        processChars(buffer.substring(0, 1), writer, key);
                        buffer.deleteCharAt(0);
                    }
                }
            }
            
            // Process remaining characters in buffer
            if (buffer.length() > 0) {
                processChars(buffer.toString(), writer, key);
            }
        } catch (FileNotFoundException e) {
            throw new IOException("File not found: " + e.getMessage(), e);
        } catch (SecurityException e) {
            throw new IOException("Security error accessing file: " + e.getMessage(), e);
        }
    }
    
    /**
     * Process a string of characters by applying the cipher
     * 
     * @param chars the characters to process
     * @param writer the writer to output the processed characters
     * @param key the shift key
     * @throws IOException if an I/O error occurs
     */
    private void processChars(String chars, BufferedWriter writer, int key) throws IOException {
        for (char ch : chars.toCharArray()) {
            if (ch >= CHAR_START) {
                writer.write(shiftCharacter(ch, key));
            } else {
                writer.write(ch);
            }
        }
    }
    
    /**
     * Shifts a character by the specified key, handling wrap-around
     * 
     * @param character the ASCII value of the character to shift
     * @param key the number of positions to shift
     * @return the new ASCII value after shifting
     */
    private int shiftCharacter(int character, int key) {
        // Normalize to our range (0 to CHAR_RANGE-1)
        int normalized = character - CHAR_START;
        
        // Apply the shift with modulo to handle wrap-around
        // Adding CHAR_RANGE before taking modulo handles negative keys properly
        int shifted = (normalized + key % CHAR_RANGE + CHAR_RANGE) % CHAR_RANGE;
        
        // Convert back to ASCII value
        return shifted + CHAR_START;
    }
    
    /**
     * Attempts to detect the encryption key by analyzing patterns in the encrypted text.
     * 
     * @param encryptedFile the file containing encrypted text
     * @return the detected encryption key, or 0 if no key could be detected
     * @throws IOException if an I/O error occurs
     */
    public int bruteForceDetectKey(String encryptedFile) throws IOException {
        System.out.println("Attempting to detect encryption key using brute force...");
        
        // First try pattern detection
        int key = detectKeyByPattern(encryptedFile);
        if (key != 0) {
            return key;
        }
        
        // If pattern detection fails, try frequency analysis
        System.out.println("No pattern found, trying frequency analysis...");
        key = detectKeyByFrequency(encryptedFile);
        if (key != 0) {
            return key;
        }
        
        // If all else fails, try all possible keys and check readability
        System.out.println("Trying all possible keys...");
        return tryAllKeys(encryptedFile);
    }
    
    /**
     * Detect key by looking for specific patterns in the text
     * 
     * @param encryptedFile the encrypted file
     * @return the detected key or 0 if not found
     * @throws IOException if an I/O error occurs
     */
    private int detectKeyByPattern(String encryptedFile) throws IOException {
        List<Integer> chars = readInitialChars(encryptedFile, 500);
        
        // Look for pairs of characters that might be period followed by line feed
        for (int i = 0; i < chars.size() - 1; i++) {
            int first = chars.get(i);
            int second = chars.get(i + 1);
            
            // Check if this distance matches our expected pattern
            if (hasExpectedDistance(first, second)) {
                int potentialKey = tryDecryptPair(first, second);
                if (potentialKey != 0) {
                    return potentialKey;
                }
            }
        }
        
        return 0; // No key detected
    }
    
    /**
     * Read the initial portion of the encrypted file for analysis
     * 
     * @param filePath the file to read from
     * @param maxChars maximum number of characters to read
     * @return List of character codes read from the file
     * @throws IOException if an I/O error occurs
     */
    private List<Integer> readInitialChars(String filePath, int maxChars) throws IOException {
        List<Integer> chars = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            int c;
            while ((c = reader.read()) != -1 && chars.size() < maxChars) {
                chars.add(c);
            }
        }
        return chars;
    }

    /**
     * Check if a character pair has the expected distance pattern
     * 
     * @param first first character in the pair
     * @param second second character in the pair
     * @return true if the distance matches expected pattern
     */
    private boolean hasExpectedDistance(int first, int second) {
        int distance = Math.abs(first - second);
        return distance == PATTERN_DISTANCE || distance == (CHAR_RANGE - PATTERN_DISTANCE);
    }

    /**
     * Try to decrypt a pair of characters to see if they match expected pattern
     * 
     * @param first first character in the pair
     * @param second second character in the pair
     * @return potential key if found, 0 otherwise
     */
    private int tryDecryptPair(int first, int second) {
        // Calculate potential key assuming first char is a period
        int potentialKey = (first - PERIOD + CHAR_RANGE) % CHAR_RANGE;
        
        // Check if second char would be a line feed with this key
        int decryptedSecond = (second - CHAR_START - potentialKey + CHAR_RANGE) % CHAR_RANGE + CHAR_START;
        
        if (decryptedSecond == LINE_FEED || decryptedSecond == CARRIAGE_RETURN) {
            return potentialKey;
        }
        
        return 0; // Not a match
    }
    
    /**
     * Detect key by analyzing character frequency in the text
     * 
     * @param encryptedFile the encrypted file
     * @return the detected key or 0 if not found
     * @throws IOException if an I/O error occurs
     */
    private int detectKeyByFrequency(String encryptedFile) throws IOException {
        // Implementation of frequency analysis
        // This is a simplified approach - in a real application, this would be more sophisticated
        List<Integer> chars = readInitialChars(encryptedFile, 1000);
        
        // Most common characters in English/Spanish text are space (32) and 'e'/'a' (101/97)
        // Count frequency of each character
        int[] frequency = new int[CHAR_RANGE];
        for (int c : chars) {
            if (c >= CHAR_START && c <= CHAR_END) {
                frequency[c - CHAR_START]++;
            }
        }
        
        // Find most frequent character
        int maxIndex = 0;
        for (int i = 1; i < frequency.length; i++) {
            if (frequency[i] > frequency[maxIndex]) {
                maxIndex = i;
            }
        }
        
        // Assume it's either space or 'e'
        int mostFreqChar = maxIndex + CHAR_START;
        int keyAssumeSpace = (mostFreqChar - 32 + CHAR_RANGE) % CHAR_RANGE;
        int keyAssumeE = (mostFreqChar - 101 + CHAR_RANGE) % CHAR_RANGE;
        
        // Try both keys and see which produces more readable text
        double scoreSpace = testKeyReadability(encryptedFile, keyAssumeSpace);
        double scoreE = testKeyReadability(encryptedFile, keyAssumeE);
        
        if (scoreSpace > scoreE && scoreSpace > MIN_READABILITY_SCORE) {
            return keyAssumeSpace;
        } else if (scoreE > MIN_READABILITY_SCORE) {
            return keyAssumeE;
        }
        
        return 0;
    }
    
    /**
     * Try all possible keys and select the one that produces the most readable text
     * 
     * @param encryptedFile the encrypted file
     * @return the best key found
     * @throws IOException if an I/O error occurs
     */
    private int tryAllKeys(String encryptedFile) throws IOException {
        int bestKey = 0;
        double bestScore = 0;
        
        // Try all possible keys (1 to CHAR_RANGE-1)
        for (int key = 1; key < CHAR_RANGE; key++) {
            double score = testKeyReadability(encryptedFile, key);
            if (score > bestScore) {
                bestScore = score;
                bestKey = key;
            }
        }
        
        return bestKey;
    }
    
    /**
     * Test how readable the text is when decrypted with a specific key
     * 
     * @param encryptedFile the encrypted file
     * @param key the key to test
     * @return readability score (0-1, higher is better)
     * @throws IOException if an I/O error occurs
     */
    private double testKeyReadability(String encryptedFile, int key) throws IOException {
        List<Integer> chars = readInitialChars(encryptedFile, 200);
        int readableChars = 0;
        
        for (int c : chars) {
            // Decrypt the character
            int decrypted = (c - CHAR_START - key + CHAR_RANGE) % CHAR_RANGE + CHAR_START;
            
            // Check if it's a common readable character
            if ((decrypted >= LOWERCASE_A && decrypted <= LOWERCASE_Z) || 
                (decrypted >= UPPERCASE_A && decrypted <= UPPERCASE_Z) || 
                decrypted == 32 || decrypted == PERIOD || decrypted == COMMA || 
                decrypted == LINE_FEED || decrypted == CARRIAGE_RETURN) {
                readableChars++;
            }
        }
        
        return (double) readableChars / chars.size();
    }
}
