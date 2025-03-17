import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Utility class for handling file operations in the encryption/decryption process.
 * 
 * This class provides a clean interface for file operations with:
 * 1. Proper character encoding (UTF-8)
 * 2. Consistent error handling
 * 3. File existence checks
 * 
 * Features:
 * - UTF-8 encoding for proper text handling
 * - Buffered I/O for efficient file operations
 * - Descriptive error messages
 * - Automatic resource management
 * 
 * Usage Example:
 * FileHandler handler = new FileHandler();
 * if (handler.fileExists("input.txt")) {
 *     String content = handler.readFileAsString("input.txt");
 *     handler.writeStringToFile("output.txt", content);
 * }
 */
public class FileHandler {
    
    /**
     * Checks if a file exists at the specified path.
     * This method is typically used before attempting read/write operations.
     * 
     * @param filePath absolute or relative path to the file
     * @return true if the file exists and is readable, false otherwise
     * @see #readFileAsString(String)
     * @see #writeStringToFile(String, String)
     */
    public boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
    
    /**
     * Reads the entire content of a file as a UTF-8 encoded string.
     * Uses buffered reading for efficient large file handling.
     * 
     * @param filePath absolute or relative path to the file
     * @return the content of the file as a UTF-8 string
     * @throws IOException if the file cannot be read, doesn't exist,
     *         or if there are insufficient permissions
     */
    public String readFileAsString(String filePath) throws IOException {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IOException("Error reading file: " + filePath, e);
        }
    }
    
    /**
     * Writes a string to a file using UTF-8 encoding.
     * Creates the file if it doesn't exist, overwrites if it does.
     * 
     * @param filePath absolute or relative path to the file
     * @param content string content to write to the file
     * @throws IOException if the file cannot be written to, parent directory
     *         doesn't exist, or if there are insufficient permissions
     */
    public void writeStringToFile(String filePath, String content) throws IOException {
        try {
            Files.write(Paths.get(filePath), content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IOException("Error writing to file: " + filePath, e);
        }
    }
    
    /**
     * Creates a BufferedReader for a file with UTF-8 encoding
     * 
     * @param filePath path to the file
     * @return a BufferedReader for the file
     * @throws IOException if an I/O error occurs
     */
    public BufferedReader getReader(String filePath) throws IOException {
        try {
            return new BufferedReader(
                new InputStreamReader(
                    new FileInputStream(filePath), StandardCharsets.UTF_8
                )
            );
        } catch (FileNotFoundException e) {
            throw new IOException("File not found: " + filePath, e);
        }
    }
    
    /**
     * Creates a BufferedWriter for a file with UTF-8 encoding
     * 
     * @param filePath path to the file
     * @return a BufferedWriter for the file
     * @throws IOException if an I/O error occurs
     */
    public BufferedWriter getWriter(String filePath) throws IOException {
        try {
            return new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream(filePath), StandardCharsets.UTF_8
                )
            );
        } catch (FileNotFoundException e) {
            throw new IOException("Cannot create file: " + filePath, e);
        }
    }
}
