import java.io.IOException;

/**
 * Main class for the File Encryption/Decryption program.
 * This class serves as the entry point for the application and
 * delegates to specialized classes for UI, encryption/decryption, and file handling.
 */
public class FileEncryptor {
    
    /**
     * Main method - entry point for the program
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            // Create and start the user interface
            EncryptorUI ui = new EncryptorUI();
            ui.start();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
