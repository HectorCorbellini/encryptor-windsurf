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

# First run - Encrypt with key 9
echo -e "\nSTEP 1: Encrypting with key 9"
echo -e "1\n9" | java -cp src FileEncryptor

# Show encrypted content
show_file "/root/my-documents/textoEncrip.txt" "Encrypted file (with key 9)"

# Copy encrypted file back to source for decryption
cp /root/my-documents/textoEncrip.txt /root/my-documents/textoPrueba.txt

# Second run - Decrypt with key -9
echo -e "\nSTEP 2: Decrypting with key -9"
echo -e "2\n9" | java -cp src FileEncryptor

# Show decrypted content
show_file "/root/my-documents/textoEncrip.txt" "Decrypted file (with key -9)"

# Restore original file
echo -e "\nRestoring original file..."
echo "El zorro viejo saltó y se escondió en la azotea. Llegaron los bomberos y tuvieron que sacarlo.
Al día siguiente los niños, que eran 14, no habían podido dormir.
Jugaron a hacer canciones y sacar cuentas matemáticas." > /root/my-documents/textoPrueba.txt

echo -e "\nTesting complete!"
