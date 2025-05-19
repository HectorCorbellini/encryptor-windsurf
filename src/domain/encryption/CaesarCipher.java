package domain.encryption;

import domain.io.FileHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import static domain.encryption.EncryptionConfig.*;

/**
 * Implementation of the Caesar Cipher encryption algorithm for text file encryption.
 * 
 * This class provides a robust implementation of the Caesar cipher that:
 * 1. Shifts each character by a specified key value in the ASCII table
 * 2. Preserves file structure (line endings, special characters)
 * 3. Handles both encryption and decryption operations
 * 4. Includes smart key detection for encrypted files
 * 
 * Features:
 * - Extended ASCII Support: Processes characters from space (32) to extended ASCII (255)
 * - Smart Key Detection: Uses multiple strategies to detect encryption keys:
 *   a) Pattern analysis of common text structures
 *   b) Character frequency analysis
 *   c) Readability scoring
 * - Error Handling: Validates inputs and provides meaningful error messages
 * - File Processing: Streams file content to handle large files efficiently
 * 
 * Usage Example:
 * CaesarCipher cipher = new CaesarCipher();
 * cipher.encryptFile("input.txt", "encrypted.txt", 5);  // Shift by 5
 * cipher.decryptFile("encrypted.txt", "decrypted.txt", -5); // Shift back by -5
 * 
 * Limitations:
 * - Text Files Only: Works only with ASCII text files (not binary)
 * - Key Range: Limited by the ASCII range (32-255)
 * - Pattern Detection: May not detect keys in very short texts
 * - Memory Usage: Loads entire file content for key detection
 */
public class CaesarCipher implements EncryptionService {
    private final FileHandler fileHandler;
    private final KeyDetector keyDetector;
    // Using centralized configuration from EncryptionConfig

    /**
     * Default constructor that creates a new FileHandler instance
     */
    public CaesarCipher() {
        this.fileHandler = new FileHandler();
        this.keyDetector = new KeyDetector(fileHandler);
    }
    
    /**
     * Constructor with dependency injection for better testability
     * 
     * @param fileHandler the file handler to use for file operations
     */
    public CaesarCipher(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        this.keyDetector = new KeyDetector(fileHandler);
    }
    
    /**
     * Constructor with full dependency injection
     * 
     * @param fileHandler the file handler to use for file operations
     * @param keyDetector the key detector to use for brute force detection
     */
    public CaesarCipher(FileHandler fileHandler, KeyDetector keyDetector) {
        this.fileHandler = fileHandler;
        this.keyDetector = keyDetector;
    }

    /**
     * Encrypts a file using Caesar cipher with the given key
     * 
     * @param sourceFile the file to encrypt
     * @param destinationFile the file to write encrypted content to
     * @param key the encryption key (how many positions to shift each character)
     * @throws IOException if an I/O error occurs
     * @throws IllegalArgumentException if the key is negative
     */
    public void encryptFile(String sourceFile, String destinationFile, int key) throws EncryptionException {
        // Validate parameters
        validateParameters(sourceFile, destinationFile, key, true);
        
        try {
            processFile(sourceFile, destinationFile, key);
        } catch (IOException e) {
            throw new EncryptionException(EncryptionException.ErrorType.IO_ERROR, "Error processing file: " + e.getMessage(), e);
        }
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
    public void decryptFile(String sourceFile, String destinationFile, int key) throws EncryptionException {
        // Validate parameters
        validateParameters(sourceFile, destinationFile, key, false);
        
        // For decryption, we simply pass the negative key directly to process file
        // This will effectively shift in the opposite direction from encryption
        try {
            processFile(sourceFile, destinationFile, key);
        } catch (IOException e) {
            throw new EncryptionException(EncryptionException.ErrorType.IO_ERROR, "Error processing file: " + e.getMessage(), e);
        }
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
        try (BufferedReader reader = fileHandler.getReader(sourceFile);
             BufferedWriter writer = fileHandler.getWriter(destinationFile)) {
            
            int character;
            while ((character = reader.read()) != -1) {
                char ch = (char) character;
                if (ch >= EncryptionConfig.CHAR_START) {
                    writer.write(shiftCharacter(ch, key));
                } else {
                    writer.write(ch);
                }
            }
        } catch (FileNotFoundException e) {
            throw new IOException("File not found: " + e.getMessage(), e);
        } catch (SecurityException e) {
            throw new IOException("Security error accessing file: " + e.getMessage(), e);
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
        int normalized = character - EncryptionConfig.CHAR_START;
        
        // Apply the shift with modulo to handle wrap-around
        int shifted = normalized + key;
        // Handle negative shifts and wrap-around in a single step
        shifted = ((shifted % EncryptionConfig.CHAR_RANGE) + EncryptionConfig.CHAR_RANGE) % EncryptionConfig.CHAR_RANGE;
        
        // Convert back to ASCII value
        return shifted + EncryptionConfig.CHAR_START;
    }
    
    /**
     * Attempts to detect the encryption key by analyzing patterns in the encrypted text.
     * 
     * @param encryptedFile the file containing encrypted text
     * @return the detected encryption key, or 0 if no key could be detected
     * @throws IOException if an I/O error occurs
     */
    public int bruteForceDetectKey(String encryptedFile) throws EncryptionException {
        return keyDetector.detectKey(encryptedFile);
    }
    
    /**
     * Validates the parameters for encryption/decryption operations
     * 
     * @param sourceFile the source file path
     * @param destinationFile the destination file path
     * @param key the encryption/decryption key
     * @param isEncryption true if this is an encryption operation, false for decryption
     * @throws EncryptionException if any parameter is invalid
     */
    private void validateParameters(String sourceFile, String destinationFile, int key, boolean isEncryption) 
            throws EncryptionException {
        // Check for null parameters
        if (sourceFile == null) {
            throw new EncryptionException(EncryptionException.ErrorType.INVALID_FILE_FORMAT, 
                    "Source file path cannot be null");
        }
        if (destinationFile == null) {
            throw new EncryptionException(EncryptionException.ErrorType.INVALID_FILE_FORMAT, 
                    "Destination file path cannot be null");
        }
        
        // Validate key based on operation type
        if (isEncryption && key < 0) {
            throw new EncryptionException(EncryptionException.ErrorType.INVALID_KEY, 
                    "Encryption key must be positive");
        } else if (!isEncryption && key > 0) {
            throw new EncryptionException(EncryptionException.ErrorType.INVALID_KEY, 
                    "Decryption key must be negative");
        }
        
        // Check if source file exists
        if (!fileHandler.fileExists(sourceFile)) {
            throw new EncryptionException(EncryptionException.ErrorType.FILE_NOT_FOUND, 
                    "Source file does not exist: " + sourceFile);
        }
    }
}
