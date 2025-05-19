package application;

import java.io.IOException;
import presentation.ui.UserInterface;
import presentation.ui.UIFactory;
import domain.encryption.EncryptionService;
import domain.encryption.CaesarCipher;

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
            EncryptionService encryptor = new CaesarCipher();
            
            // Create and start the user interface based on user preference
            UserInterface ui = UIFactory.createUI(encryptor);
            ui.start();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            if (!(e instanceof IOException)) {
                e.printStackTrace();
            }
        }
    }
}
