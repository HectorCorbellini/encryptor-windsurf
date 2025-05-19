package presentation.ui;

import java.io.IOException;
import domain.encryption.EncryptionException;

/**
 * Interface defining the user interface operations for the encryption application.
 * This allows for different UI implementations (console, GUI) while maintaining
 * consistent functionality.
 */
public interface UserInterface {
    /**
     * Starts the user interface and handles user interaction
     * 
     * @throws IOException if there is an error during file operations
     */
    void start() throws IOException, EncryptionException;
    
    /**
     * Displays a message to the user
     * 
     * @param message the message to display
     */
    void displayMessage(String message);
    
    /**
     * Gets user input with a prompt
     * 
     * @param prompt the prompt to display
     * @return the user's input
     */
    String getUserInput(String prompt);
    
    /**
     * Gets an integer input from the user within a specified range
     * 
     * @param prompt the prompt to display
     * @param min minimum valid value
     * @param max maximum valid value
     * @return the user's input as an integer
     */
    int getIntegerInput(String prompt, int min, int max);
    
    /**
     * Displays file content preview
     * 
     * @param content the content to preview
     * @param title the title for the preview
     */
    void displayPreview(String content, String title);
}
