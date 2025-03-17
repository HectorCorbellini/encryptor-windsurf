import java.io.IOException;
import java.util.Scanner;

/**
 * User interface for the file encryption/decryption application.
 * This class handles user interaction and delegates operations to the appropriate classes.
 */
public class EncryptorUI {
    private static final String DEFAULT_SOURCE_FILE = "/root/my-documents/textoPrueba.txt";
    private static final String DEFAULT_ENCRYPTED_FILE = "/root/my-documents/textoEncrip.txt";
    private static final String DEFAULT_DECRYPTED_FILE = "/root/my-documents/textoDecrypted.txt";
    
    private final CaesarCipher cipher;
    private final Scanner scanner;
    
    /**
     * Constructor initializes the UI with required dependencies
     */
    public EncryptorUI() {
        this.cipher = new CaesarCipher();
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Starts the user interface
     */
    public void start() {
        displayHeader();
        int choice = getUserChoice();
        
        try {
            processUserChoice(choice);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
    
    /**
     * Displays the application header
     */
    private void displayHeader() {
        System.out.println("======== FILE ENCRYPTION/DECRYPTION PROGRAM ========");
        System.out.println("1. Encrypt a file");
        System.out.println("2. Decrypt a file (with known key)");
        System.out.println("3. Brute Force Decrypt (without key)");
    }
    
    /**
     * Gets the user's menu choice
     * 
     * @return the user's choice (1, 2, or 3)
     */
    private int getUserChoice() {
        while (true) {
            System.out.print("Enter your choice (1, 2, or 3): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 1 && choice <= 3) {
                    return choice;
                } else {
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
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
        }
    }
    
    /**
     * Handles the encryption process
     * 
     * @throws IOException if an I/O error occurs
     */
    private void handleEncryption() throws IOException {
        int key = getEncryptionKey();
        
        try {
            cipher.encryptFile(DEFAULT_SOURCE_FILE, DEFAULT_ENCRYPTED_FILE, key);
            System.out.println("File encrypted successfully as " + DEFAULT_ENCRYPTED_FILE);
        } catch (IOException e) {
            throw new IOException("Failed to encrypt file: " + e.getMessage(), e);
        }
    }
    
    /**
     * Handles the decryption process with a known key
     * 
     * @throws IOException if an I/O error occurs
     */
    private void handleDecryption() throws IOException {
        int key = getDecryptionKey();
        
        try {
            cipher.decryptFile(DEFAULT_ENCRYPTED_FILE, DEFAULT_DECRYPTED_FILE, key);
            System.out.println("File decrypted successfully as " + DEFAULT_DECRYPTED_FILE);
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
            int detectedKey = cipher.bruteForceDetectKey(DEFAULT_ENCRYPTED_FILE);
            
            if (detectedKey == 0) {
                System.out.println("Could not detect encryption key. Decryption failed.");
                return;
            }
            
            System.out.println("Detected encryption key: " + detectedKey);
            cipher.decryptFile(DEFAULT_ENCRYPTED_FILE, DEFAULT_DECRYPTED_FILE, -detectedKey);
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
}
