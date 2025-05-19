package domain.encryption;

import domain.io.FileHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static domain.encryption.EncryptionConfig.*;

/**
 * Class responsible for detecting encryption keys in encrypted files.
 * This class implements the Single Responsibility Principle by separating
 * key detection logic from the encryption/decryption logic.
 */
public class KeyDetector {
    // Using centralized configuration from EncryptionConfig
    
    private final FileHandler fileHandler;
    
    /**
     * Creates a new KeyDetector with the specified file handler
     * 
     * @param fileHandler the file handler to use for file operations
     */
    public KeyDetector(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }
    
    /**
     * Attempts to detect the encryption key by analyzing patterns in the encrypted text.
     * 
     * @param encryptedFile the file containing encrypted text
     * @return the detected encryption key, or 0 if no key could be detected
     * @throws EncryptionException if an error occurs during key detection
     */
    public int detectKey(String encryptedFile) throws EncryptionException {
        System.out.println("Attempting to detect encryption key using brute force...");
        
        try {
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
            key = tryAllKeys(encryptedFile);
            
            if (key == 0) {
                throw new EncryptionException(EncryptionException.ErrorType.KEY_DETECTION_FAILED, 
                        "Could not detect encryption key for file: " + encryptedFile);
            }
            
            return key;
        } catch (IOException e) {
            throw new EncryptionException(EncryptionException.ErrorType.IO_ERROR, 
                    "Error reading file during key detection: " + e.getMessage(), e);
        }
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
        try (var reader = fileHandler.getReader(filePath)) {
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
        // Try assuming first char is PERIOD and second is LINE_FEED
        int keyForPeriod = (first - EncryptionConfig.PERIOD + EncryptionConfig.CHAR_RANGE) % EncryptionConfig.CHAR_RANGE;
        int decryptedSecond = (second - EncryptionConfig.CHAR_START - keyForPeriod + EncryptionConfig.CHAR_RANGE) % EncryptionConfig.CHAR_RANGE + EncryptionConfig.CHAR_START;
        
        if (decryptedSecond == EncryptionConfig.LINE_FEED) {
            return keyForPeriod;
        }
        
        return 0; // No key found
    }
    
    /**
     * Detect key using frequency analysis of characters
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
        int[] frequency = new int[EncryptionConfig.CHAR_RANGE];
        for (int c : chars) {
            if (c >= EncryptionConfig.CHAR_START && c <= EncryptionConfig.CHAR_END) {
                frequency[c - EncryptionConfig.CHAR_START]++;
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
        int mostFreqChar = maxIndex + EncryptionConfig.CHAR_START;
        int keyAssumeSpace = (mostFreqChar - 32 + EncryptionConfig.CHAR_RANGE) % EncryptionConfig.CHAR_RANGE;
        int keyAssumeE = (mostFreqChar - 101 + EncryptionConfig.CHAR_RANGE) % EncryptionConfig.CHAR_RANGE;
        
        // Try both keys and see which produces more readable text
        double scoreSpace = testKeyReadability(encryptedFile, keyAssumeSpace);
        double scoreE = testKeyReadability(encryptedFile, keyAssumeE);
        
        if (scoreSpace > scoreE && scoreSpace > EncryptionConfig.MIN_READABILITY_SCORE) {
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
        for (int key = 1; key < EncryptionConfig.CHAR_RANGE; key++) {
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
            int decrypted = (c - EncryptionConfig.CHAR_START - key + EncryptionConfig.CHAR_RANGE) % EncryptionConfig.CHAR_RANGE + EncryptionConfig.CHAR_START;
            
            // Check if it's a common readable character
            if ((decrypted >= EncryptionConfig.LOWERCASE_A && decrypted <= EncryptionConfig.LOWERCASE_Z) || 
                (decrypted >= EncryptionConfig.UPPERCASE_A && decrypted <= EncryptionConfig.UPPERCASE_Z) || 
                decrypted == 32 || decrypted == EncryptionConfig.PERIOD || decrypted == EncryptionConfig.COMMA || 
                decrypted == EncryptionConfig.LINE_FEED || decrypted == EncryptionConfig.CARRIAGE_RETURN) {
                readableChars++;
            }
        }
        
        return (double) readableChars / chars.size();
    }
}
