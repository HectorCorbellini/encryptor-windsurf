# Caesar Cipher Encryption Tool

## Overview
This is a Java-based file encryption/decryption application implementing the Caesar cipher method. The application provides a modern, interactive web interface for easy file encryption and decryption operations. It can be used as a standalone application or embedded within another web application, such as a portfolio website. The codebase has been refactored to follow Clean Architecture principles and optimized for maintainability and extensibility.

## Features
- Encrypt and decrypt text files using Caesar cipher
- Brute force key detection
- Modern web-based user interface with:
  - Real-time operation feedback
  - Visual indication of current operation
  - Live file preview updates
  - Dark mode UI design
- Supports ASCII characters (range 32-255)
- Handles special characters and Unicode text
- Embeddable as a widget in other web applications
- RESTful API for programmatic access
- Clean Architecture implementation with:
  - Separation of concerns (domain, application, presentation layers)
  - Dependency injection for better testability
  - Consistent error handling with custom exceptions
  - Externalized configuration
  - Proper exception propagation and handling

## Prerequisites
- Java Development Kit (JDK) 8 or higher
- Python 3
- Web browser

## Project Structure
- `src/` - Java source files
  - `domain/` - Domain layer (core business logic)
    - `encryption/` - Encryption services (EncryptionService, CaesarCipher, KeyDetector, EncryptionConfig, EncryptionException)
    - `io/` - I/O utilities (FileHandler)
  - `application/` - Application layer (EncryptorApp.java, WebAppLauncher.java)
  - `presentation/ui/` - Presentation/UI layer (ConsoleUI, EncryptorUI, WebUI, UIFactory, UserInterface)
  - `resources/` - Localization and configuration files (message bundles for internationalization)
- `bin/` - Compiled .class files
- `web/` - Web interface files
  - `api.py` - RESTful API server for integration
  - `encryptor-widget.js` - Embeddable JavaScript widget
  - `integration-example.html` - Example of portfolio integration
- `test/` - Test files and scripts
  - `test_encryption_updated.sh` - Script for testing encryption functionality
- `run.sh` - Launcher script for easy startup
- `start_server.sh` - Script to start the web server
- `embedded_server.sh` - Script for embedded mode in other applications
- `INTEGRATION.md` - Detailed integration instructions

## Architecture and Design Patterns

This application follows Clean Architecture principles to ensure maintainability, testability, and separation of concerns:

### Layers
- **Domain Layer**: Contains the core business logic and entities
  - `EncryptionService`: Interface defining encryption operations
  - `CaesarCipher`: Implementation of the Caesar cipher algorithm
  - `KeyDetector`: Responsible for detecting encryption keys
  - `EncryptionConfig`: Centralizes configuration values
  - `EncryptionException`: Custom exception hierarchy for domain-specific errors

- **Application Layer**: Coordinates the domain layer with the presentation layer
  - `EncryptorApp`: Main application entry point
  - `WebAppLauncher`: Entry point for web-based usage

- **Presentation Layer**: Handles user interaction
  - `UserInterface`: Interface for different UI implementations
  - `ConsoleUI`, `WebUI`: Concrete UI implementations
  - `UIFactory`: Factory for creating UI instances

### Design Patterns
- **Dependency Injection**: Components receive their dependencies through constructors
- **Factory Method**: `UIFactory` creates appropriate UI implementations
- **Strategy Pattern**: Different encryption algorithms can implement `EncryptionService`
- **Single Responsibility Principle**: Each class has one reason to change

## Running the Application

### Using the Launcher Script (Easiest)
1. Clone the repository
2. Navigate to the project directory
3. Make the scripts executable:
   ```bash
   chmod +x run.sh start_server.sh embedded_server.sh
   ```
4. Run the launcher script:
   ```bash
   ./run.sh
   ```
5. The web interface will automatically start

### Web Interface (Recommended)
1. Clone the repository
2. Navigate to the project directory
3. Make the server script executable:
   ```bash
   chmod +x start_server.sh
   ```
4. Run the server script directly:
   ```bash
   ./start_server.sh
   ```
5. Open a web browser and go to `http://localhost:8080`

### Integration with Other Web Applications
The encryption tool can be embedded in other web applications, such as a portfolio website. For detailed integration instructions, see the `INTEGRATION.md` file.

Quick integration steps:

1. Start the embedded server:
   ```bash
   ./embedded_server.sh
   ```

2. Include the widget in your HTML:
   ```html
   <script src="path/to/encryptor-widget.js"></script>
   <div id="encryptor-container"></div>
   <script>
     const encryptor = new EncryptorWidget();
     encryptor.init('encryptor-container');
   </script>
   ```

### How to Use
1. Select "Input File" to choose the file you want to encrypt/decrypt
2. Select "Output File" to specify where to save the result
3. Enter the encryption/decryption key (not required for Brute Force)
4. Choose your operation:
   - Encrypt: Shift characters forward by the specified key
   - Decrypt: Shift characters backward by the specified key
   - Brute Force: Automatically detect the encryption key
5. View the results:
   - The active operation button will be highlighted in red
   - The file preview will automatically update to show the result
   - Use the "View Input" and "View Output" buttons to compare files

## Security Note
The Caesar cipher is a simple substitution cipher and is NOT suitable for protecting sensitive information. It is primarily used for educational purposes.

## Contributing
Please read the CONTRIBUTING.md for details on our code of conduct and the process for submitting pull requests.

## License
[Specify your license here]

## Acknowledgments
- Inspired by classic cryptography techniques
- Developed as an educational tool to understand basic encryption principles
