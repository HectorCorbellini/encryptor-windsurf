#!/bin/bash

# Compile the Java program
echo "Compiling Java program..."
javac src/FileEncryptor.java

# Function to show file content
show_file() {
    local file=$1
    local desc=$2
    echo -e "\n$desc:"
    echo "----------------------------------------"
    cat "$file"
    echo "----------------------------------------"
}

# Show original content
echo -e "\nSTEP 0: Original content"
show_file "/root/my-documents/textoPrueba.txt" "Original file"

# First run - Encrypt with key 15 (we'll pretend we don't know this key later)
echo -e "\nSTEP 1: Encrypting with key 15"
echo -e "1\n15" | java -cp src FileEncryptor

# Show encrypted content
show_file "/root/my-documents/textoEncrip.txt" "Encrypted file (with key 15)"

# Second run - Brute Force Decrypt (without knowing the key)
echo -e "\nSTEP 2: Attempting Brute Force Decryption (without knowing the key)"
echo -e "3" | java -cp src FileEncryptor

# Show decrypted content
show_file "/root/my-documents/textoDecrypted.txt" "Decrypted file (using brute force)"

echo -e "\nBrute Force Testing complete!"
