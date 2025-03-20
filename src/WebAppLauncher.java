import ui.CaesarCipher;
import ui.EncryptionService;
import ui.WebUI;

/**
 * Entry point for the web-based version of the encryption application.
 * This class provides static methods that can be called from JavaScript
 * to interact with the encryption functionality.
 */
public class WebAppLauncher {
    private static WebUI webUI;
    
    /**
     * Initializes the encryption application for web use
     * 
     * @return true if initialization was successful
     */
    public static boolean initialize() {
        try {
            EncryptionService encryptor = new CaesarCipher();
            webUI = new WebUI(encryptor);
            return true;
        } catch (Exception e) {
            System.err.println("Error initializing web application: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Sets the parameters for encryption/decryption operation
     * 
     * @param inputFile the input file path
     * @param outputFile the output file path
     * @param key the encryption/decryption key (can be null for brute force)
     * @param mode the operation mode (1=encrypt, 2=decrypt, 3=brute force)
     * @return true if parameters were set successfully
     */
    public static boolean setParameters(String inputFile, String outputFile, String key, int mode) {
        if (webUI == null) {
            if (!initialize()) {
                return false;
            }
        }
        
        try {
            webUI.setParameters(inputFile, outputFile, key, mode);
            return true;
        } catch (Exception e) {
            System.err.println("Error setting parameters: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Executes the encryption/decryption operation with current parameters
     * 
     * @return true if the operation was successful
     */
    public static boolean executeOperation() {
        if (webUI == null) {
            return false;
        }
        
        return webUI.executeOperation();
    }
    
    /**
     * Gets the content of a file for display in the web interface
     * 
     * @param filePath the path to the file
     * @return the file content as a string
     */
    public static String getFileContent(String filePath) {
        if (webUI == null) {
            if (!initialize()) {
                return "Error: Application not initialized";
            }
        }
        
        return webUI.getFileContent(filePath);
    }
    
    /**
     * Sets a callback function to receive output messages
     * This method would be implemented differently when using a specific
     * Java-to-JavaScript bridge technology
     * 
     * @param callbackReference a reference to a JavaScript callback function
     */
    public static void setOutputCallback(Object callbackReference) {
        // This is a placeholder - actual implementation depends on the
        // Java-to-JavaScript bridge technology being used
        if (webUI != null) {
            webUI.setOutputCallback(message -> {
                // Call JavaScript callback with message
                // Implementation depends on the bridge technology
                System.out.println("Web output: " + message);
            });
        }
    }
}
