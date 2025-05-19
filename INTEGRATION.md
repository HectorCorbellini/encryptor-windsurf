# Integrating the Caesar Cipher Encryption Tool into a Web Portfolio

This document provides instructions for integrating the Caesar Cipher Encryption Tool into your web portfolio application.

## Overview

The Caesar Cipher Encryption Tool has been refactored using Clean Architecture principles and is designed to work as an embeddable component within another web application. The codebase has been optimized for maintainability, with redundant files removed and exception handling improved. The integration uses:

1. A Python API server that communicates with the Java backend
2. A JavaScript widget that can be embedded in any web page
3. Improved startup scripts with automatic port management
4. A well-structured domain layer with clear separation of concerns

## Integration Steps

### 1. Prepare the Encryption Tool

1. Copy the entire `Encripte-` directory into your project.
2. Make the launcher and server scripts executable:
   ```bash
   chmod +x embedded_server.sh start_server.sh run.sh
   ```

### 2. Start the Encryption Service

The `embedded_server.sh` script compiles Java packages, automatically terminates any existing server on the same port, and starts the Python API server.

- **Manual startup:**
  ```bash
  cd /path/to/Encripte-
  ./embedded_server.sh [PORT]
  ```
  - Defaults to port `8080` if no port is given.
  - The script prints the Java/Python process PID for programmatic control.

- **Programmatic startup (Node.js):**
  ```javascript
  const { exec } = require('child_process');
  const proc = exec('/path/to/Encripte-/embedded_server.sh 8080');
  proc.stdout.on('data', data => {
    const pid = parseInt(data.trim());
    console.log(`Encryption service started with PID: ${pid}`);
    // store PID to kill later
  });
  ```

### 3. Include the Widget in Your Portfolio

1. Copy `web/encryptor-widget.js` into your web assets.
2. Add to your HTML page:
   ```html
   <script src="/path/to/encryptor-widget.js"></script>
   <div id="encryptor-container"></div>
   <script>
     const encryptor = new EncryptorWidget('http://localhost:8080');
     encryptor.init('encryptor-container');
   </script>
   ```

### 4. Modal Integration Example

Copy the modal markup from `web/integration-example.html` and invoke the widget inside the modal's open event.

## Clean Architecture Benefits for Integration

The refactored codebase offers several advantages for integration:

1. **Improved Error Handling**: The `EncryptionException` hierarchy provides detailed, user-friendly error messages that can be displayed in your UI. Exception propagation has been optimized for better error reporting.

2. **Dependency Injection**: If you need to extend or customize the encryption functionality, you can inject your own implementations of `FileHandler` or create a new implementation of `EncryptionService`.

3. **Centralized Configuration**: All constants and configuration values are in `EncryptionConfig`, making it easy to adjust parameters without modifying multiple files.

4. **Separation of Concerns**: The clear separation between domain logic (encryption), application coordination, and presentation makes it easier to understand and modify specific parts of the system.

5. **Extensibility**: You can add new encryption algorithms by implementing the `EncryptionService` interface without changing the rest of the application.

6. **Optimized Codebase**: Redundant files and unused code have been removed, resulting in a cleaner, more maintainable codebase.

7. **Streamlined Resource Management**: All resource files (like message bundles) are now in a standardized location.

## API Endpoints

The encryption service provides the following API endpoints:

- `GET /api/status` - Check if the service is running
- `POST /api/encrypt` - Encrypt text
  - Request body: `{ "text": "Text to encrypt", "key": 3 }`
- `POST /api/decrypt` - Decrypt text
  - Request body: `{ "text": "Encrypted text", "key": 3 }`

## Customization

You can customize the widget's appearance by modifying the CSS in `encryptor-widget.js`.

## Troubleshooting

1. **Service not starting**: Make sure Java is installed and the paths in `embedded_server.sh` are correct
2. **CORS errors**: The API server includes CORS headers, but you may need to adjust them for your specific domain
3. **Port conflicts**: If port 8080 is already in use, the scripts will automatically terminate any process using that port. Alternatively, you can specify a different port as a parameter to the scripts
4. **Compilation errors**: If you encounter any Java compilation errors, check that you're using JDK 8 or higher
5. **Exception handling**: The application now has improved exception handling, with specific exceptions for different error scenarios

## Example

See `web/integration-example.html` for a complete working example of the integration.
