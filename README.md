# File Encryption/Decryption with Caesar Cipher

This Java application implements a file encryption and decryption system using the Caesar cipher method. The program allows users to encrypt text files by shifting ASCII characters by a specified key value, and then decrypt them either using the same key or through a brute force method that automatically detects the key.

## Recent Improvements

- **Interface-Based Design**: Added `Encryptor` interface for better extensibility and maintainability
- **Dependency Injection**: Improved testability and flexibility through constructor injection
- **Enhanced Error Handling**: Better error messages and console output visibility
- **Improved File Initialization**: Automatic creation of necessary directories and sample files
- **Console Output**: Better handling of console output and user interaction
- **Code Organization**: More modular structure with clear separation of concerns

## How It Works

### Caesar Cipher

The Caesar cipher is one of the simplest and most widely known encryption techniques. It works by shifting each character in the plaintext by a certain number of places down the alphabet. In this implementation:

- We use ASCII characters starting from code 32 (space) up to 255
- Each character is shifted by the specified key value
- If the shift exceeds the ASCII table range, it wraps around to the beginning of the used range

For example:
- If the character is 'A' (ASCII 65) and the key is 3, the encrypted character will be 'D' (ASCII 68)
- If the character is 'ü' (ASCII 252) and the key is 9, the encrypted character will be '%' (ASCII 37, after wrapping around)

### Encryption Process

1. The program reads the source file (`textoPrueba.txt`)
2. Each character is shifted forward by the key value
3. The encrypted content is written to the destination file (`textoEncrip.txt`)

### Decryption Process

1. The program reads the encrypted file (`textoEncrip.txt`)
2. Each character is shifted backward by the key value (using a negative key)
3. The decrypted content is written to the output file (`textoDecrypted.txt`)

### Brute Force Decryption Process

1. The program reads the encrypted file (`textoEncrip.txt`)
2. It analyzes the first 500 characters to detect patterns in the text
3. The algorithm looks for the specific pattern of a period (.) followed by a carriage return, which has a consistent distance of 31 in the ASCII table
4. If this pattern is found, the program calculates the likely encryption key
5. If no pattern is found, the program tries all possible keys and selects the one that produces the most readable text
6. The file is decrypted using the detected key and written to the output file (`textoDecrypted.txt`)

## How to Use

### Using the Build Script

The application comes with a convenient build script that handles compilation, execution, and testing:

1. Compile the Java program:
   ```bash
   ./build.sh compile
   ```

2. Run the application:
   ```bash
   ./build.sh run
   ```

3. Run the unit tests:
   ```bash
   ./build.sh test
   ```

4. Clean, compile, and run in one step:
   ```bash
   ./build.sh all
   ```

5. Clean, compile, and run tests in one step:
   ```bash
   ./build.sh testall
   ```

### Manual Execution

1. Compile the Java program:
   ```bash
   javac -d bin src/*.java
   ```

2. Run the program:
   ```bash
   java -cp bin EncryptorApp
   ```

3. Follow the prompts:
   - Enter `1` for encryption, `2` for decryption with a known key, or `3` for brute force decryption
   - If you chose option 1 or 2, enter the key value (positive for encryption, negative for decryption)
   - The program will automatically adjust the key if needed
   - For brute force decryption, no key is needed as the program will attempt to detect it automatically

### Running Automated Tests

The application comes with several automated test scripts that verify different aspects of functionality:

#### Basic Encryption/Decryption Test
```bash
./test_encryption.sh
```
This script tests the basic encryption and decryption process with a key of 9:
- Compiles all Java files into the bin directory
- Encrypts a sample text file using a key of 9
- Decrypts the encrypted file using a key of -9
- Displays the original, encrypted, and decrypted content for comparison

#### Advanced Encryption/Decryption Test
```bash
./test_encryption_updated.sh
```
This script tests encryption and decryption with a larger key (29):
- Compiles all Java files into the bin directory
- Encrypts a sample text file using a key of 29
- Decrypts the encrypted file using a key of -29
- Displays the original, encrypted, and decrypted content for comparison

#### Brute Force Decryption Test
```bash
./test_brute_force.sh
```
This script tests the automatic key detection and brute force decryption:
- Compiles all Java files into the bin directory
- Encrypts a sample text file using a key of 15 (pretending we don't know this key)
- Uses the brute force decryption algorithm to automatically detect the key
- Displays the original, encrypted, and decrypted content for comparison

#### Comprehensive Test Suite
```bash
./test_all.sh
```
This script runs all the above tests in sequence and provides a summary of results:
- Compiles the application
- Runs all test scripts
- Verifies content integrity across all tests
- Reports success or failure for each test case

## Project Structure

### Directories
- `src/`: Contains all Java source files
- `bin/`: Contains compiled class files

### Source Files
- `src/Encryptor.java`: Interface defining encryption operations
- `src/EncryptorApp.java`: Main application entry point
- `src/CaesarCipher.java`: Implements Encryptor interface with Caesar cipher logic
- `src/FileHandler.java`: Manages file operations
- `src/EncryptorUI.java`: Handles user interface and interaction
- `src/CaesarCipherTest.java`: Unit tests for the cipher functionality

### Build and Test Scripts
- `build.sh`: Build script for compiling, running, and testing
- `test_encryption.sh`: Tests basic encryption/decryption with key 9
- `test_encryption_updated.sh`: Tests encryption/decryption with key 29
- `test_brute_force.sh`: Tests automatic key detection and brute force decryption
- `test_all.sh`: Comprehensive automated test script that runs all tests

### Input/Output Files (in `/root/my-documents/`)
- `textoPrueba.txt`: Original text file to encrypt
- `textoEncrip.txt`: Encrypted output file
- `textoDecrypted.txt`: Decrypted output file

## Security Considerations

The Caesar cipher is a very basic encryption method and is not secure for sensitive information. It's primarily used for educational purposes or for basic obfuscation. For serious security applications, consider using modern encryption algorithms like AES or RSA.

## Language Support

The application is designed to work with both English and Spanish text. The brute force decryption feature analyzes common patterns in these languages to detect the encryption key automatically.

## Code Architecture

### Clean Code Principles Applied

The code has been refactored following clean code principles:

- **Single Responsibility Principle**: Each class has a clear, focused responsibility
  - `CaesarCipher`: Handles only encryption/decryption logic
  - `FileHandler`: Manages only file operations
  - `EncryptorUI`: Handles only user interaction
  - `EncryptorApp`: Serves as the application entry point

- **Object-Oriented Design**: Proper encapsulation and class organization
  - Classes have private fields and public methods with clear interfaces
  - Dependencies are injected through constructors
  - Each class exposes only what is necessary

- **Error Handling**: More robust exception handling
  - Specific exception types with descriptive messages
  - Proper propagation of exceptions to the appropriate level
  - User-friendly error messages

### Testing

The application includes comprehensive testing:

- **Unit Tests**: `CaesarCipherTest` class verifies core functionality
  - Tests encryption with known keys
  - Tests decryption with known keys
  - Tests brute force decryption

- **Integration Tests**: The `test_all.sh` script tests the entire application
  - Verifies the semantic correctness of encryption/decryption
  - Provides robustness against encoding differences
  - Calculates similarity percentages to ensure high-quality results
