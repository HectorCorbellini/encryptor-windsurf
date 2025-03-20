#!/bin/bash

# Caesar Cipher Encryption Tool Launcher Script

echo "Caesar Cipher Encryption Tool Launcher"
echo "======================================"
echo ""
echo "1. Start Web Interface (Recommended)"
echo "2. Start Legacy Swing Interface"
echo "3. Exit"
echo ""
read -p "Enter your choice (1-3): " choice

case $choice in
    1)
        # Start web interface
        echo "Starting web interface..."
        ./start_server.sh
        ;;
    2)
        # Start legacy Swing interface
        echo "Starting legacy Swing interface..."
        
        # Ensure bin directory exists
        mkdir -p bin
        
        # Compile Java classes
        javac -d bin src/*.java src/ui/*.java src/ui/legacy/*.java
        
        # Run with GUI mode
        java -Dui.mode=gui -cp bin EncryptorApp
        ;;
    3)
        echo "Exiting..."
        exit 0
        ;;
    *)
        echo "Invalid choice. Exiting..."
        exit 1
        ;;
esac
