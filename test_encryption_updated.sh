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

# First run - Encrypt with key 29
echo -e "\nSTEP 1: Encrypting with key 29"
echo -e "1\n29" | java -cp src FileEncryptor

# Show encrypted content
show_file "/root/my-documents/textoEncrip.txt" "Encrypted file (with key 29)"

# Second run - Decrypt with key 29
echo -e "\nSTEP 2: Decrypting with key 29"
echo -e "2\n29" | java -cp src FileEncryptor

# Show decrypted content
show_file "/root/my-documents/textoDecrypted.txt" "Decrypted file (with key -29)"

echo -e "\nTesting complete!"
