# Caesar Cipher Encryption Tool

## Overview
This is a Java-based file encryption/decryption application implementing the Caesar cipher method. The application provides a modern, interactive web interface for easy file encryption and decryption operations.

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

## Prerequisites
- Java Development Kit (JDK) 8 or higher
- Python 3
- Web browser

## Project Structure
- `src/` - Contains all Java source files
  - `ui/` - User interface implementations
    - `legacy/` - Contains the legacy Swing UI
  - `WebAppLauncher.java` - Entry point for web interface
  - `EncryptorApp.java` - Main application entry point
- `bin/` - Contains compiled .class files
- `web/` - Web interface files
- `run.sh` - Launcher script for easy startup
- `start_server.sh` - Script to start the web server

## Running the Application

### Using the Launcher Script (Easiest)
1. Clone the repository
2. Navigate to the project directory
3. Run the launcher script:
   ```bash
   ./run.sh
   ```
4. Choose your preferred interface from the menu:
   - Option 1: Web Interface (Recommended)
   - Option 2: Legacy Swing Interface
   - Option 3: Exit

### Web Interface (Recommended)
1. Clone the repository
2. Navigate to the project directory
3. Run the server script directly:
   ```bash
   ./start_server.sh
   ```
4. Open a web browser and go to `http://localhost:8080`

### Legacy Swing Interface
The project includes a legacy Swing UI interface that was part of the original application. To use this interface:

1. Set the system property when running the app:
   ```bash
   java -Dui.mode=gui -cp bin EncryptorApp
   ```

This is maintained for backward compatibility, but the web interface is recommended for most users.

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
