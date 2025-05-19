package domain.encryption;

import java.io.IOException;

/**
 * Interface defining encryption operations.
 * This allows for easy addition of new encryption algorithms
 * while maintaining a consistent API.
 */
public interface EncryptionService {
    /**
     * Encrypts a file using the specified key
     * 
     * @param sourceFile path to the source file
     * @param destinationFile path to save the encrypted content
     * @param key encryption key
     * @throws IOException if an I/O error occurs
     */
    void encryptFile(String sourceFile, String destinationFile, int key) throws EncryptionException;
    
    /**
     * Decrypts a file using the specified key
     * 
     * @param sourceFile path to the encrypted file
     * @param destinationFile path to save the decrypted content
     * @param key decryption key
     * @throws IOException if an I/O error occurs
     */
    void decryptFile(String sourceFile, String destinationFile, int key) throws EncryptionException;
    
    /**
     * Attempts to detect the encryption key using various methods
     * 
     * @param encryptedFile path to the encrypted file
     * @return detected key, or 0 if detection failed
     * @throws IOException if an I/O error occurs
     */
    int bruteForceDetectKey(String encryptedFile) throws EncryptionException;
}
