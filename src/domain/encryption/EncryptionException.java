package domain.encryption;

/**
 * Custom exception for encryption-related errors.
 * Provides specific error types and consistent error handling across the application.
 */
public class EncryptionException extends Exception {
    
    /**
     * Error types for encryption operations
     */
    public enum ErrorType {
        INVALID_KEY,
        FILE_NOT_FOUND,
        PERMISSION_DENIED,
        IO_ERROR,
        INVALID_FILE_FORMAT,
        KEY_DETECTION_FAILED
    }
    
    private final ErrorType errorType;
    
    /**
     * Creates a new EncryptionException with the specified error type
     * 
     * @param errorType the type of error that occurred
     * @param message detailed error message
     */
    public EncryptionException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }
    
    /**
     * Creates a new EncryptionException with the specified error type and cause
     * 
     * @param errorType the type of error that occurred
     * @param message detailed error message
     * @param cause the underlying exception that caused this error
     */
    public EncryptionException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }
    
    /**
     * Gets the error type for this exception
     * 
     * @return the error type
     */
    public ErrorType getErrorType() {
        return errorType;
    }
    
    /**
     * Gets a user-friendly error message based on the error type
     * 
     * @return a user-friendly error message
     */
    public String getUserFriendlyMessage() {
        switch (errorType) {
            case INVALID_KEY:
                return "The encryption key is invalid. Please use a positive number for encryption and a negative number for decryption.";
            case FILE_NOT_FOUND:
                return "The specified file could not be found. Please check the file path and try again.";
            case PERMISSION_DENIED:
                return "Permission denied when accessing the file. Please check file permissions.";
            case IO_ERROR:
                return "An error occurred while reading or writing the file.";
            case INVALID_FILE_FORMAT:
                return "The file format is not supported. Please use a text file.";
            case KEY_DETECTION_FAILED:
                return "Could not detect the encryption key. The file may not be encrypted with Caesar cipher.";
            default:
                return "An unknown error occurred during encryption/decryption.";
        }
    }
}
