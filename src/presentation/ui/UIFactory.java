package presentation.ui;

import domain.encryption.EncryptionService;

/**
 * Factory class for creating user interface instances.
 * Provides a unified way to create different UI implementations.
 */
public class UIFactory {
    /**
     * UI types supported by the application
     */
    public enum UIType {
        CONSOLE,
        WEB
    }
    
    /**
     * Creates a UserInterface instance based on user preference or system property
     * 
     * @param encryptor the encryption implementation to use
     * @return the selected UserInterface implementation
     */
    public static UserInterface createUI(EncryptionService encryptor) {
        // Check for system property first (for automated testing)
        String uiMode = System.getProperty("ui.mode");
        if (uiMode != null) {
            if (uiMode.equalsIgnoreCase("console")) {
                return createUI(encryptor, UIType.CONSOLE);
            } else if (uiMode.equalsIgnoreCase("web")) {
                return createUI(encryptor, UIType.WEB);
            }
        }
        
        // Default to web UI without prompting
        return createUI(encryptor, UIType.WEB);
    }
    
    /**
     * Creates a specific type of UserInterface
     * 
     * @param encryptor the encryption implementation to use
     * @param type the type of UI to create
     * @return the selected UserInterface implementation
     */
    public static UserInterface createUI(EncryptionService encryptor, UIType type) {
        switch (type) {
            case WEB:
                return new WebUI(encryptor);
            case CONSOLE:
            default:
                return new ConsoleUI(encryptor);
        }
    }
}
