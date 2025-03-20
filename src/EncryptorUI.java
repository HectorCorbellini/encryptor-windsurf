package ui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;
import ui.EncryptionService;
import ui.CaesarCipher;

/**
 * Enhanced user interface for the file encryption/decryption application.
 * This class provides an interactive console interface with file selection,
 * content preview, and detailed operation feedback.
 */
public class EncryptorUI {
    public static final String DEFAULT_SOURCE_FILE = "test/textoPrueba.txt";
    public static final String DEFAULT_ENCRYPTED_FILE = "test/textoEncrip.txt";
    public static final String DEFAULT_DECRYPTED_FILE = "test/textoDecrypted.txt";
    private static final int PREVIEW_LENGTH = 150; // Number of characters to preview
    
    private final EncryptionService encryptor;
    private final Scanner scanner;
    private String currentSourceFile;
    private String currentEncryptedFile;
    private String currentDecryptedFile;
    
    /**
     * Default constructor that uses CaesarCipher and System.in
     */
    public EncryptorUI() {
        this(new CaesarCipher(), new Scanner(System.in),
             DEFAULT_SOURCE_FILE, DEFAULT_ENCRYPTED_FILE, DEFAULT_DECRYPTED_FILE);
    }

    /**
     * Constructor with dependency injection for better testability and flexibility
     * 
     * @param encryptor the encryption implementation to use
     * @param scanner the input scanner to use
     * @param sourceFile default source file path
     * @param encryptedFile default encrypted file path
     * @param decryptedFile default decrypted file path
     */
    public EncryptorUI(EncryptionService encryptor, Scanner scanner,
                      String sourceFile, String encryptedFile, String decryptedFile) {
        this.encryptor = encryptor;
        this.scanner = scanner;
        this.currentSourceFile = sourceFile;
        this.currentEncryptedFile = encryptedFile;
        this.currentDecryptedFile = decryptedFile;
        initializeFiles();
    }
    
    /**
     * Initializes the necessary files in the current directory
     */
    private void initializeFiles() {
        try {
            // Create the files if they don't exist
            if (!new File(currentSourceFile).exists()) {
                String sampleText = "Este es un archivo de prueba para la aplicación de encriptación.\n" +
                                  "Contiene texto en español y caracteres especiales: áéíóúñ.\n" +
                                  "El cifrado de César se aplicará a este contenido.";
                Files.write(Paths.get(currentSourceFile), sampleText.getBytes(StandardCharsets.UTF_8));
            }
            // Create empty files for encrypted and decrypted content
            if (!new File(currentEncryptedFile).exists()) {
                Files.createFile(Paths.get(currentEncryptedFile));
            }
            if (!new File(currentDecryptedFile).exists()) {
                Files.createFile(Paths.get(currentDecryptedFile));
            }
        } catch (IOException e) {
            displayMessage("Error initializing files: " + e.getMessage());
        }
    }
    
    /**
     * Starts the user interface
     */
    /**
     * Starts the encryption/decryption program UI.
     * Handles both interactive and automated (testing) modes.
     */
    public void start() {
        System.out.println("Starting File Encryption/Decryption Program...");
        System.out.flush();
        
        boolean running = true;
        while (running) {
            try {
                clearScreen();
                displayHeader();
                
                // Ensure prompt is visible
                System.out.print("\nEnter your choice (0-5): ");
                System.out.flush();
                
                // Check if we still have input available
                if (!scanner.hasNext()) {
                    running = false;
                    continue;
                }
                
                int choice = getUserChoice();
                if (choice == 0) {
                    running = false;
                    continue;
                }
                
                processUserChoice(choice);
                waitForEnter();
            } catch (NoSuchElementException e) {
                // End of input stream reached (normal in automated testing)
                running = false;
            } catch (IllegalStateException e) {
                // Scanner closed (can happen during shutdown)
                running = false;
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
                System.err.flush();
                waitForEnter();
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid input: " + e.getMessage());
                System.err.flush();
                waitForEnter();
            }
        }
        
        System.out.println("\nThank you for using the File Encryption/Decryption Program!");
        System.out.flush();
    }
    
    /**
     * Displays the application header with enhanced menu options
     */
    private void displayHeader() {
        String header = "========== FILE ENCRYPTION/DECRYPTION PROGRAM ==========\n" +
                       "\nCurrent working files:\n" +
                       "Source: " + currentSourceFile + "\n" +
                       "Encrypted: " + currentEncryptedFile + "\n" +
                       "Decrypted: " + currentDecryptedFile + "\n" +
                       "\nAvailable operations:\n" +
                       "1. Encrypt a file\n" +
                       "2. Decrypt a file (with known key)\n" +
                       "3. Brute Force Decrypt (without key)\n" +
                       "4. Change input/output files\n" +
                       "5. View file contents\n" +
                       "0. Exit\n" +
                       "\n================================================\n";
        System.out.print(header);
        System.out.flush();
    }
    
    /**
     * Gets the user's menu choice
     * 
     * @return the user's choice (0-5)
     */
    /**
     * Gets the user's menu choice with improved error handling and input validation
     * 
     * @return the user's choice (0-5)
     * @throws NoSuchElementException if no input is available
     */
    private int getUserChoice() {
        String input = scanner.nextLine().trim();
        
        try {
            int choice = Integer.parseInt(input);
            if (choice >= 0 && choice <= 5) {
                return choice;
            } else {
                System.out.println("Invalid choice '" + input + "'. Please enter a number between 0 and 5.");
                System.out.flush();
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input '" + input + "'. Please enter a number.");
            System.out.flush();
        }
        
        // If we get here, the input was invalid
        return -1;
    }
    
    /**
     * Processes the user's choice
     * 
     * @param choice the user's menu choice
     * @throws IOException if an I/O error occurs
     */
    private void processUserChoice(int choice) throws IOException {
        switch (choice) {
            case 1:
                handleEncryption();
                break;
            case 2:
                handleDecryption();
                break;
            case 3:
                handleBruteForceDecryption();
                break;
            case 4:
                handleFileSelection();
                break;
            case 5:
                handleFileViewing();
                break;
        }
    }
    
    /**
     * Handles the encryption process with file preview
     * 
     * @throws IOException if an I/O error occurs
     */
    private void handleEncryption() throws IOException {
        System.out.println("\n=== File Encryption ===\n");
        previewFileContent(currentSourceFile, "Source file preview");
        
        int key = getEncryptionKey();
        
        try {
            encryptor.encryptFile(currentSourceFile, currentEncryptedFile, key);
            System.out.println("\nFile encrypted successfully!");
            System.out.println("Source: " + currentSourceFile);
            System.out.println("Output: " + currentEncryptedFile);
            System.out.println("\nEncrypted file preview:");
            previewFileContent(currentEncryptedFile, null);
        } catch (IOException e) {
            throw new IOException("Failed to encrypt file: " + e.getMessage(), e);
        }
    }
    
    /**
     * Handles the decryption process with file preview
     * 
     * @throws IOException if an I/O error occurs
     */
    private void handleDecryption() throws IOException {
        System.out.println("\n=== File Decryption ===\n");
        previewFileContent(currentEncryptedFile, "Encrypted file preview");
        
        int key = getDecryptionKey();
        
        try {
            encryptor.decryptFile(currentEncryptedFile, currentDecryptedFile, key);
            System.out.println("\nFile decrypted successfully!");
            System.out.println("Source: " + currentEncryptedFile);
            System.out.println("Output: " + currentDecryptedFile);
            System.out.println("\nDecrypted file preview:");
            previewFileContent(currentDecryptedFile, null);
        } catch (IOException e) {
            throw new IOException("Failed to decrypt file: " + e.getMessage(), e);
        }
    }
    
    /**
     * Handles the brute force decryption process
     * 
     * @throws IOException if an I/O error occurs
     */
    private void handleBruteForceDecryption() throws IOException {
        try {
            int detectedKey = encryptor.bruteForceDetectKey(currentEncryptedFile);
            
            if (detectedKey == 0) {
                System.out.println("Could not detect encryption key. Decryption failed.");
                return;
            }
            
            System.out.println("Detected encryption key: " + detectedKey);
            encryptor.decryptFile(currentEncryptedFile, currentDecryptedFile, -detectedKey);
            System.out.println("File decrypted successfully as " + DEFAULT_DECRYPTED_FILE);
        } catch (IOException e) {
            throw new IOException("Failed to decrypt file: " + e.getMessage(), e);
        }
    }
    
    /**
     * Gets the encryption key from the user
     * 
     * @return the encryption key (positive integer)
     */
    private int getEncryptionKey() {
        while (true) {
            System.out.print("Enter the key value (positive for encryption, negative for decryption): ");
            try {
                int key = Integer.parseInt(scanner.nextLine().trim());
                if (key > 0) {
                    return key;
                } else {
                    System.out.println("For encryption, please enter a positive key value.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    /**
     * Gets the decryption key from the user
     * 
     * @return the decryption key (negative integer)
     */
    private int getDecryptionKey() {
        while (true) {
            System.out.print("Enter the key value (positive for encryption, negative for decryption): ");
            try {
                int key = Integer.parseInt(scanner.nextLine().trim());
                if (key < 0) {
                    return key;
                } else {
                    // Convert positive key to negative for decryption
                    return -key;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Clears the console screen
     */
    private void clearScreen() {
        // Use simple line breaks for better compatibility
        System.out.println("\n\n\n");
        System.out.flush();
    }

    /**
     * Waits for user to press Enter
     */
    /**
     * Waits for user to press Enter, with timeout for automated testing
     */
    private void waitForEnter() {
        try {
            System.out.print("\nPress Enter to continue...");
            // For automated testing, continue if no input is available
            if (!scanner.hasNextLine()) {
                return;
            }
            scanner.nextLine();
        } catch (NoSuchElementException e) {
            // Input stream closed or end of input reached
            // This is normal for automated testing
        } catch (IllegalStateException e) {
            // Scanner closed
            // This can happen during program shutdown
        }
    }

    /**
     * Handles file selection for input and output files
     */
    /**
     * Handles the file selection menu and user interaction
     */
    private void handleFileSelection() {
        showFileSelectionMenu();
        processFileSelectionChoice();
    }

    /**
     * Displays the file selection menu options
     */
    private void showFileSelectionMenu() {
        System.out.println("\n=== File Selection ===\n");
        System.out.println("1. Change source file");
        System.out.println("2. Change encrypted file location");
        System.out.println("3. Change decrypted file location");
        System.out.println("4. Return to main menu");
    }

    /**
     * Processes the user's file selection choice
     */
    private void processFileSelectionChoice() {
        while (true) {
            System.out.print("\nEnter your choice (1-4): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice == 4) break;

                if (choice >= 1 && choice <= 3) {
                    updateFilePath(choice);
                    break;
                } else {
                    System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Updates the file path based on user selection
     * 
     * @param choice the user's menu choice (1-3)
     */
    private void updateFilePath(int choice) {
        System.out.print("Enter the new file path: ");
        String newPath = scanner.nextLine().trim();
        updateFilePathForChoice(choice, newPath);
    }

    /**
     * Updates the appropriate file path based on the menu choice
     * 
     * @param choice the user's menu choice (1-3)
     * @param newPath the new file path
     */
    private void updateFilePathForChoice(int choice, String newPath) {
        switch (choice) {
            case 1:
                currentSourceFile = newPath;
                System.out.println("Source file updated successfully!");
                break;
            case 2:
                currentEncryptedFile = newPath;
                System.out.println("Encrypted file location updated successfully!");
                break;
            case 3:
                currentDecryptedFile = newPath;
                System.out.println("Decrypted file location updated successfully!");
                break;
        }
    }

    /**
     * Handles viewing of file contents
     */
    /**
     * Handles the file viewing menu and user interaction
     */
    private void handleFileViewing() {
        showFileViewingMenu();
        processFileViewingChoice();
    }

    /**
     * Displays the file viewing menu options
     */
    private void showFileViewingMenu() {
        System.out.println("\n=== View File Contents ===\n");
        System.out.println("1. View source file");
        System.out.println("2. View encrypted file");
        System.out.println("3. View decrypted file");
        System.out.println("4. Return to main menu");
    }

    /**
     * Processes the user's file viewing choice
     */
    private void processFileViewingChoice() {
        while (true) {
            System.out.print("\nEnter your choice (1-4): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice == 4) break;

                if (choice >= 1 && choice <= 3) {
                    displayFileContent(choice);
                    break;
                } else {
                    System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Displays the content of the selected file
     * 
     * @param choice the user's menu choice (1-3)
     */
    private void displayFileContent(int choice) {
        try {
            FileInfo fileInfo = getFileInfo(choice);
            System.out.println("\n=== " + fileInfo.type + " File Contents ===\n");
            previewFileContent(fileInfo.path, null);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Helper class to store file information
     */
    private static class FileInfo {
        final String path;
        final String type;

        FileInfo(String path, String type) {
            this.path = path;
            this.type = type;
        }
    }

    /**
     * Gets the file information based on the menu choice
     * 
     * @param choice the user's menu choice (1-3)
     * @return FileInfo containing the file path and type
     */
    private FileInfo getFileInfo(int choice) {
        String path = choice == 1 ? currentSourceFile :
                      choice == 2 ? currentEncryptedFile :
                      currentDecryptedFile;
        String type = choice == 1 ? "Source" :
                      choice == 2 ? "Encrypted" :
                      "Decrypted";
        return new FileInfo(path, type);
    }

    /**
     * Previews the content of a file
     * 
     * @param filePath path to the file to preview
     * @param header optional header to display before the preview
     * @throws IOException if an I/O error occurs
     */
    private void previewFileContent(String filePath, String header) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File does not exist: " + filePath);
            return;
        }

        if (header != null) {
            System.out.println(header + ":");
        }

        String content = Files.readString(file.toPath());
        if (content.length() > PREVIEW_LENGTH) {
            System.out.println(content.substring(0, PREVIEW_LENGTH) + "...");
            System.out.println("[Content truncated, showing first " + PREVIEW_LENGTH + " characters]");
        } else {
            System.out.println(content);
        }
    }
    
    /**
     * Displays a message to the user
     * 
     * @param message the message to display
     */
    private void displayMessage(String message) {
        System.out.println(message);
        System.out.flush();
    }
}
