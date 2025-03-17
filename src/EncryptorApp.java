

import java.io.IOException;
import java.util.Scanner;

/**
 * Main application class for the File Encryption/Decryption program.
 * This class serves as the entry point for the application and
 * configures the encryption system components.
 */
public class EncryptorApp {
    
    /**
     * Main method - entry point for the program
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            // Create the encryption implementation
            Encryptor encryptor = new CaesarCipher();
            
            // Create and start the user interface with the encryptor
            EncryptorUI ui = new EncryptorUI(encryptor, new Scanner(System.in),
                    EncryptorUI.DEFAULT_SOURCE_FILE,
                    EncryptorUI.DEFAULT_ENCRYPTED_FILE,
                    EncryptorUI.DEFAULT_DECRYPTED_FILE);
            ui.start();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            if (!(e instanceof IOException)) {
                e.printStackTrace();
            }
        }
    }
}
