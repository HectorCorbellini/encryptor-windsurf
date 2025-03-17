# Contributing Guidelines

Welcome to the File Encryption project! This guide will help you understand how to run, modify, and contribute to this project effectively.

## Development Setup

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Git for version control
- A text editor or IDE (e.g., VSCode, IntelliJ IDEA, Eclipse)

### Project Structure
```
encryptor-windsurf/
├── src/                    # Source files
│   ├── EncryptorApp.java  # Main application entry point
│   ├── CaesarCipher.java  # Encryption/decryption logic
│   ├── FileHandler.java   # File operations
│   ├── EncryptorUI.java   # User interface
│   └── CaesarCipherTest.java  # Unit tests
├── bin/                    # Compiled class files
├── build.sh               # Build and run script
└── test_all.sh           # Automated test script
```

## Building and Running

### Using Build Script
The project includes a convenient build script with several commands:

```bash
# Compile the project
./build.sh compile

# Run the application
./build.sh run

# Run unit tests
./build.sh test

# Clean and compile
./build.sh all

# Clean, compile, and run tests
./build.sh testall
```

### Manual Build and Run
If you prefer manual compilation:

```bash
# Compile
javac -d bin src/*.java

# Run
java -cp bin EncryptorApp
```

## Testing

### Running Tests
1. Unit Tests:
   ```bash
   java -cp bin CaesarCipherTest
   ```

2. Integration Tests:
   ```bash
   ./test_all.sh
   ```

### Test Files
- Test files are stored in `/root/my-documents/`
- Default test file: `textoPrueba.txt`
- Encrypted output: `textoEncrip.txt`
- Decrypted output: `textoDecrypted.txt`

## Code Style Guidelines

### General Principles
1. **Single Responsibility**: Each class should have one clear purpose
   - `CaesarCipher`: Handles encryption/decryption
   - `FileHandler`: Manages file operations
   - `EncryptorUI`: Handles user interaction

2. **Clean Code Practices**
   - Use meaningful variable and method names
   - Keep methods short and focused
   - Add JavaDoc comments for public methods
   - Handle exceptions appropriately

3. **File Organization**
   - Keep source files in `src/`
   - Compiled files go to `bin/`
   - Test files separate from source

### Naming Conventions
- Classes: PascalCase (e.g., `CaesarCipher`)
- Methods/Variables: camelCase (e.g., `encryptFile`)
- Constants: UPPER_SNAKE_CASE (e.g., `CHAR_RANGE`)

## Making Changes

### Before Making Changes
1. Run all tests to ensure they pass
2. Create a new branch for your changes
3. Review the existing code structure

### When Making Changes
1. Follow existing code style
2. Add/update tests for new functionality
3. Update documentation if needed
4. Test thoroughly before committing

### After Making Changes
1. Run all tests again
2. Update README.md if necessary
3. Commit with descriptive messages
4. Push to your branch and create a pull request

## Security Considerations

1. **Token Handling**
   - Never commit tokens or credentials
   - Use environment variables for sensitive data
   - Follow the `.gitignore` patterns

2. **Code Security**
   - Validate all user inputs
   - Handle file operations safely
   - Close resources properly
   - Use proper exception handling

## Common Issues and Solutions

### File Paths
- Use absolute paths carefully
- Consider platform-independent paths
- Handle file not found scenarios

### Character Encoding
- Use UTF-8 for file operations
- Handle special characters properly
- Test with various text encodings

### Memory Usage
- Close file streams after use
- Use try-with-resources
- Handle large files efficiently

## Getting Help

If you encounter issues:
1. Check the test output
2. Review the documentation
3. Look for similar issues in the repository
4. Create a detailed issue report if needed

Remember: This is an educational project demonstrating basic encryption concepts. For real security applications, use established encryption libraries and follow current security best practices.
