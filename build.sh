#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Project directories
SRC_DIR="src"
BIN_DIR="bin"

# Ensure bin directory exists
mkdir -p $BIN_DIR

# Clean compiled files
clean() {
    echo "Cleaning compiled files..."
    rm -rf $BIN_DIR/*
    echo -e "${GREEN}Clean completed${NC}"
}

# Compile the project
compile() {
    echo "Compiling Java files..."
    javac -d $BIN_DIR $SRC_DIR/*.java
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}Compilation successful${NC}"
    else
        echo -e "${RED}Compilation failed${NC}"
        exit 1
    fi
}

# Run the application
run() {
    echo "Running the application..."
    java -cp $BIN_DIR EncryptorApp
}

# Run the unit tests
test() {
    echo "Running unit tests..."
    java -cp $BIN_DIR CaesarCipherTest
}

# Display usage information
usage() {
    echo "Usage: $0 [command]"
    echo "Commands:"
    echo "  clean    - Remove compiled files"
    echo "  compile  - Compile the project"
    echo "  run      - Run the application"
    echo "  test     - Run the unit tests"
    echo "  all      - Clean, compile, and run"
    echo "  testall  - Clean, compile, and run all tests"
}

# Process command line arguments
case "$1" in
    clean)
        clean
        ;;
    compile)
        compile
        ;;
    run)
        compile
        run
        ;;
    test)
        compile
        test
        ;;
    all)
        clean
        compile
        run
        ;;
    testall)
        clean
        compile
        test
        ;;
    *)
        usage
        ;;
esac
