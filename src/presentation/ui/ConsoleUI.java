package presentation.ui;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import domain.encryption.EncryptionException;
import domain.encryption.EncryptionService;

/**
 * Console-based implementation of the UserInterface.
 * Handles user interaction through command line interface.
 */
public class ConsoleUI implements UserInterface {
    private final Scanner scanner;
    private final EncryptionService encryptor;
    private String currentSourceFile;
    private String currentEncryptedFile;
    private String currentDecryptedFile;
    
    public static final String DEFAULT_SOURCE_FILE = "test/textoPrueba.txt";
    public static final String DEFAULT_ENCRYPTED_FILE = "test/textoEncrip.txt";
    public static final String DEFAULT_DECRYPTED_FILE = "test/textoDecrypted.txt";
    private static final int PREVIEW_LENGTH = 150;

    /**
     * Creates a new ConsoleUI with default settings
     * 
     * @param encryptor the encryption implementation to use
     */
    public ConsoleUI(EncryptionService encryptor) {
        this(encryptor, new Scanner(System.in),
             DEFAULT_SOURCE_FILE, DEFAULT_ENCRYPTED_FILE, DEFAULT_DECRYPTED_FILE);
    }

    /**
     * Creates a new ConsoleUI with custom settings
     * 
     * @param encryptor the encryption implementation to use
     * @param scanner the input scanner
     * @param sourceFile the source file path
     * @param encryptedFile the encrypted file path
     * @param decryptedFile the decrypted file path
     */
    public ConsoleUI(EncryptionService encryptor, Scanner scanner,
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

    @Override
    public void start() throws IOException {
        boolean isTestMode = System.getProperty("ui.mode") != null;
        
        while (true) {
            if (!isTestMode) {
                clearScreen();
                displayHeader();
            }
            
            int choice = getIntegerInput("\nEnter your choice (0-5): ", 0, 5);
            
            try {
                switch (choice) {
                    case 0:
                        if (!isTestMode) {
                            displayMessage("\nThank you for using the File Encryption/Decryption Program!");
                        }
                        return;
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
                        handleFileContentsView();
                        break;
                }
                
                if (choice != 0 && !isTestMode) {
                    displayMessage("\nPress Enter to continue...");
                    scanner.nextLine();
                }
            } catch (EncryptionException e) {
                displayMessage("Encryption Error: " + e.getUserFriendlyMessage());
                if (!isTestMode) {
                    displayMessage("Press Enter to continue...");
                    scanner.nextLine();
                }
            } catch (IOException e) {
                displayMessage("I/O Error: " + e.getMessage());
                if (!isTestMode) {
                    displayMessage("Press Enter to continue...");
                    scanner.nextLine();
                }
            }
        }
    }

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
        System.out.flush();
    }

    @Override
    public String getUserInput(String prompt) {
        boolean isTestMode = System.getProperty("ui.mode") != null;
        if (!isTestMode) {
            System.out.print(prompt);
            System.out.flush();
        }
        String input = scanner.nextLine().trim();
        if (isTestMode) {
            displayMessage("[Test Mode] Input: " + input);
        }
        return input;
    }

    @Override
    public int getIntegerInput(String prompt, int min, int max) {
        while (true) {
            try {
                String input = getUserInput(prompt);
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                displayMessage("Please enter a number between " + min + " and " + max);
            } catch (NumberFormatException e) {
                displayMessage("Please enter a valid number");
            }
        }
    }

    @Override
    public void displayPreview(String content, String title) {
        displayMessage("\n=== " + title + " ===\n");
        displayMessage(content.substring(0, Math.min(content.length(), PREVIEW_LENGTH)));
        displayMessage("\n");
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void displayHeader() {
        displayMessage("\n========== FILE ENCRYPTION/DECRYPTION PROGRAM ==========\n");
        displayMessage("Current working files:");
        displayMessage("Source: " + currentSourceFile);
        displayMessage("Encrypted: " + currentEncryptedFile);
        displayMessage("Decrypted: " + currentDecryptedFile + "\n");
        displayMessage("Available operations:");
        displayMessage("1. Encrypt a file");
        displayMessage("2. Decrypt a file (with known key)");
        displayMessage("3. Brute Force Decrypt (without key)");
        displayMessage("4. Change input/output files");
        displayMessage("5. View file contents");
        displayMessage("0. Exit\n");
        displayMessage("================================================\n");
    }

    private void handleEncryption() throws IOException, EncryptionException {
        displayPreview(new String(Files.readAllBytes(Paths.get(currentSourceFile))), "File Preview");
        int key = getIntegerInput("Enter the key value (positive for encryption, negative for decryption): ", -255, 255);
        
        encryptor.encryptFile(currentSourceFile, currentEncryptedFile, key);
        displayMessage("\nFile encrypted successfully!");
        displayMessage("Source: " + currentSourceFile);
        displayMessage("Output: " + currentEncryptedFile);
        
        String preview = new String(Files.readAllBytes(Paths.get(currentEncryptedFile)));
        displayPreview(preview, "Encrypted Content");
    }
    
    private void handleDecryption() throws IOException, EncryptionException {
        displayPreview(new String(Files.readAllBytes(Paths.get(currentEncryptedFile))), "Encrypted Content");
        int key = getIntegerInput("Enter the key value (positive for encryption, negative for decryption): ", -255, 255);
        
        encryptor.decryptFile(currentEncryptedFile, currentDecryptedFile, key);
        displayMessage("\nFile decrypted successfully!");
        displayMessage("Source: " + currentEncryptedFile);
        displayMessage("Output: " + currentDecryptedFile);
        
        String preview = new String(Files.readAllBytes(Paths.get(currentDecryptedFile)));
        displayPreview(preview, "Decrypted Content");
    }
    
    private void handleBruteForceDecryption() throws IOException, EncryptionException {
        boolean isTestMode = System.getProperty("ui.mode") != null;
        if (!isTestMode) {
            displayMessage("Attempting to detect encryption key using brute force...");
        }
        int detectedKey = encryptor.bruteForceDetectKey(currentEncryptedFile);
        
        if (detectedKey == 0) {
            displayMessage("Could not detect encryption key. Decryption failed.");
            return;
        }
        
        displayMessage("Detected encryption key: " + detectedKey);
        encryptor.decryptFile(currentEncryptedFile, currentDecryptedFile, -detectedKey);
        displayMessage("File decrypted successfully as " + currentDecryptedFile);
    }
    
    private void handleFileSelection() {
        displayMessage("\n=== File Selection ===\n");
        displayMessage("Current files:");
        displayMessage("1. Source file: " + currentSourceFile);
        displayMessage("2. Encrypted file: " + currentEncryptedFile);
        displayMessage("3. Decrypted file: " + currentDecryptedFile);
        displayMessage("0. Back to main menu");
        
        int choice = getIntegerInput("\nSelect file to change (0-3): ", 0, 3);
        if (choice == 0) return;
        
        String newPath = getUserInput("Enter new file path: ");
        switch (choice) {
            case 1:
                currentSourceFile = newPath;
                break;
            case 2:
                currentEncryptedFile = newPath;
                break;
            case 3:
                currentDecryptedFile = newPath;
                break;
        }
        
        try {
            File file = new File(newPath);
            file.getParentFile().mkdirs();
            displayMessage("File path updated successfully!");
        } catch (Exception e) {
            displayMessage("Warning: Could not create directory for the new path");
        }
    }
    
    private void handleFileContentsView() throws IOException {
        displayMessage("\n=== File Contents ===\n");
        
        if (Files.exists(Paths.get(currentSourceFile))) {
            displayMessage("\nSource file contents:");
            displayPreview(new String(Files.readAllBytes(Paths.get(currentSourceFile))), "Source Content");
        }
        
        if (Files.exists(Paths.get(currentEncryptedFile))) {
            displayMessage("\nEncrypted file contents:");
            displayPreview(new String(Files.readAllBytes(Paths.get(currentEncryptedFile))), "Encrypted Content");
        }
        
        if (Files.exists(Paths.get(currentDecryptedFile))) {
            displayMessage("\nDecrypted file contents:");
            displayPreview(new String(Files.readAllBytes(Paths.get(currentDecryptedFile))), "Decrypted Content");
        }
    }
}
