package ui;

import java.io.IOException;
import java.util.function.Consumer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

/**
 * Web-based user interface implementation for the encryption application.
 * This class is designed to be used when the application is embedded in a web page.
 */
public class WebUI implements UserInterface {
    private final EncryptionService encryptor;
    private Consumer<String> outputCallback;
    private String inputSource;
    private String outputDestination;
    private String keyValue;
    private int operationMode = -1; // Default: no operation selected
    
    /**
     * Constructs a WebUI with the specified encryption implementation
     * 
     * @param encryptor the encryption implementation to use
     */
    public WebUI(EncryptionService encryptor) {
        this.encryptor = encryptor;
        this.inputSource = "test/textoPrueba.txt";
        this.outputDestination = "test/textoEncrip.txt";
    }
    
    /**
     * Sets the callback function for sending output back to the web page
     * 
     * @param callback the function to call with output messages
     */
    public void setOutputCallback(Consumer<String> callback) {
        this.outputCallback = callback;
    }
    
    /**
     * Sets the input parameters for the encryption/decryption operation
     * 
     * @param inputSource the source file path
     * @param outputDestination the destination file path
     * @param key the encryption/decryption key (can be null for brute force)
     * @param mode the operation mode (1=encrypt, 2=decrypt, 3=brute force)
     */
    public void setParameters(String inputSource, String outputDestination, String key, int mode) {
        this.inputSource = inputSource;
        this.outputDestination = outputDestination;
        this.keyValue = key;
        this.operationMode = mode;
    }
    
    /**
     * Executes the encryption/decryption operation with the current parameters
     * 
     * @return true if the operation was successful, false otherwise
     */
    public boolean executeOperation() {
        try {
            switch (operationMode) {
                case 1: // Encrypt
                    int encryptKey = Integer.parseInt(keyValue);
                    encryptor.encryptFile(inputSource, outputDestination, encryptKey);
                    displayMessage("File encrypted successfully.");
                    return true;
                    
                case 2: // Decrypt with key
                    int decryptKey = Integer.parseInt(keyValue);
                    encryptor.decryptFile(inputSource, outputDestination, decryptKey);
                    displayMessage("File decrypted successfully.");
                    return true;
                    
                case 3: // Brute force decrypt
                    int detectedKey = encryptor.bruteForceDetectKey(inputSource);
                    encryptor.decryptFile(inputSource, outputDestination, detectedKey);
                    displayMessage("File decrypted successfully with key: " + detectedKey);
                    return true;
                    
                default:
                    displayMessage("Invalid operation mode.");
                    return false;
            }
        } catch (NumberFormatException e) {
            displayMessage("Invalid key format: " + e.getMessage());
            return false;
        } catch (IOException e) {
            displayMessage("Error during file operation: " + e.getMessage());
            return false;
        } catch (Exception e) {
            displayMessage("Unexpected error: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public void start() throws IOException {
        // This method is not used in web mode as operations are triggered by web events
        displayMessage("Web UI initialized. Operations will be triggered by web events.");
    }

    @Override
    public void displayMessage(String message) {
        if (outputCallback != null) {
            outputCallback.accept(message);
        } else {
            System.out.println(message); // Fallback if callback not set
        }
    }

    @Override
    public String getUserInput(String prompt) {
        // In web mode, input comes from form fields, not interactive prompts
        throw new UnsupportedOperationException("Interactive input not supported in web mode");
    }

    @Override
    public int getIntegerInput(String prompt, int min, int max) {
        // In web mode, input comes from form fields, not interactive prompts
        throw new UnsupportedOperationException("Interactive input not supported in web mode");
    }

    @Override
    public void displayPreview(String content, String title) {
        if (outputCallback != null) {
            outputCallback.accept(title + ":\n" + content);
        }
    }
    
    /**
     * Gets the content of a file for display in the web interface
     * 
     * @param filePath the path to the file
     * @return the file content as a string
     */
    public String getFileContent(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "Error reading file: " + e.getMessage();
        }
    }
}
