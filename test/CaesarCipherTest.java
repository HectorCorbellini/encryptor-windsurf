import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import ui.CaesarCipher;

/**
 * Test class for the CaesarCipher implementation.
 * This class contains unit tests for encryption, decryption, and brute force functionality.
 */
public class CaesarCipherTest {
    private static final String TEST_DIR = "/root/my-documents/test/";
    private static final String TEST_ORIGINAL = TEST_DIR + "test_original.txt";
    private static final String TEST_ENCRYPTED = TEST_DIR + "test_encrypted.txt";
    private static final String TEST_DECRYPTED = TEST_DIR + "test_decrypted.txt";
    
    private final CaesarCipher cipher = new CaesarCipher();
    private final FileHandler fileHandler = new FileHandler();
    
    /**
     * Main method to run all tests
     */
    public static void main(String[] args) {
        CaesarCipherTest test = new CaesarCipherTest();
        
        try {
            // Ensure test directory exists
            Files.createDirectories(Paths.get(TEST_DIR));
            
            // Create test file with sample content
            test.createTestFile();
            
            // Run tests
            test.testEncryption();
            test.testDecryption();
            test.testBruteForce();
            
            System.out.println("All tests passed successfully!");
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Creates a test file with sample content
     */
    private void createTestFile() throws IOException {
        String content = "This is a test file with some special characters: áéíóú ñ\n" +
                         "It also contains punctuation marks: .,;:!?\n" +
                         "And some ellipses... which should be preserved.\n" +
                         "The end.";
        
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(TEST_ORIGINAL), StandardCharsets.UTF_8))) {
            writer.write(content);
        }
        
        System.out.println("Created test file with sample content");
    }
    
    /**
     * Tests the encryption functionality
     */
    private void testEncryption() throws IOException {
        System.out.println("\nTesting encryption...");
        
        // Test with key = 5
        int key = 5;
        cipher.encryptFile(TEST_ORIGINAL, TEST_ENCRYPTED, key);
        
        // Verify the encrypted file exists and is different from original
        if (!fileHandler.fileExists(TEST_ENCRYPTED)) {
            throw new AssertionError("Encrypted file was not created");
        }
        
        String original = fileHandler.readFileAsString(TEST_ORIGINAL);
        String encrypted = fileHandler.readFileAsString(TEST_ENCRYPTED);
        
        if (original.equals(encrypted)) {
            throw new AssertionError("Encrypted file is identical to original");
        }
        
        System.out.println("✓ Encryption test passed");
    }
    
    /**
     * Tests the decryption functionality
     */
    private void testDecryption() throws IOException {
        System.out.println("\nTesting decryption with known key...");
        
        // Test with key = -5 (inverse of encryption key)
        int key = -5;
        cipher.decryptFile(TEST_ENCRYPTED, TEST_DECRYPTED, key);
        
        // Verify the decrypted file exists and matches the original
        if (!fileHandler.fileExists(TEST_DECRYPTED)) {
            throw new AssertionError("Decrypted file was not created");
        }
        
        String original = fileHandler.readFileAsString(TEST_ORIGINAL);
        String decrypted = fileHandler.readFileAsString(TEST_DECRYPTED);
        
        // Compare content (ignoring potential line ending differences)
        if (!compareContent(original, decrypted)) {
            throw new AssertionError("Decrypted content does not match original");
        }
        
        System.out.println("✓ Decryption test passed");
    }
    
    /**
     * Tests the brute force decryption functionality
     */
    private void testBruteForce() throws IOException {
        System.out.println("\nTesting brute force decryption...");
        
        // Delete previous decrypted file if it exists
        Files.deleteIfExists(Paths.get(TEST_DECRYPTED));
        
        // Detect key and decrypt
        int detectedKey = cipher.bruteForceDetectKey(TEST_ENCRYPTED);
        
        if (detectedKey == 0) {
            throw new AssertionError("Failed to detect encryption key");
        }
        
        System.out.println("Detected key: " + detectedKey);
        cipher.decryptFile(TEST_ENCRYPTED, TEST_DECRYPTED, -detectedKey);
        
        // Verify the decrypted file exists and matches the original
        if (!fileHandler.fileExists(TEST_DECRYPTED)) {
            throw new AssertionError("Brute force decrypted file was not created");
        }
        
        String original = fileHandler.readFileAsString(TEST_ORIGINAL);
        String decrypted = fileHandler.readFileAsString(TEST_DECRYPTED);
        
        // Compare content (ignoring potential line ending differences)
        if (!compareContent(original, decrypted)) {
            throw new AssertionError("Brute force decrypted content does not match original");
        }
        
        System.out.println("✓ Brute force decryption test passed");
    }
    
    /**
     * Compares the content of two strings, ignoring line ending differences
     * 
     * @param str1 first string
     * @param str2 second string
     * @return true if the content is semantically equivalent
     */
    private boolean compareContent(String str1, String str2) {
        // Normalize line endings
        String normalized1 = str1.replaceAll("\\r\\n|\\r|\\n", "\n");
        String normalized2 = str2.replaceAll("\\r\\n|\\r|\\n", "\n");
        
        // Compare normalized strings
        return normalized1.equals(normalized2);
    }
}
