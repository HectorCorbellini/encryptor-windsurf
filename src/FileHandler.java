import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Utility class for file operations related to the encryption/decryption process.
 * This class handles reading from and writing to files.
 */
public class FileHandler {
    
    /**
     * Checks if a file exists
     * 
     * @param filePath path to the file
     * @return true if the file exists, false otherwise
     */
    public boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
    
    /**
     * Reads the content of a file as a string
     * 
     * @param filePath path to the file
     * @return the content of the file as a string
     * @throws IOException if an I/O error occurs
     */
    public String readFileAsString(String filePath) throws IOException {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IOException("Error reading file: " + filePath, e);
        }
    }
    
    /**
     * Writes a string to a file
     * 
     * @param filePath path to the file
     * @param content content to write
     * @throws IOException if an I/O error occurs
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
