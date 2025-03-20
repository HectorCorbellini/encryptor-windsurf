#!/bin/bash

# Ensure bin directory exists
mkdir -p bin

# Compile Java classes
javac -d bin src/*.java src/ui/*.java src/ui/legacy/*.java

# Forcefully kill any process using port 8080
fuser -k 8080/tcp 2>/dev/null || true

# Create temporary server directory if it doesn't exist
mkdir -p /tmp/server

# Copy web files to temporary server directory
cp -r web/* /tmp/server/ 2>/dev/null || echo "Warning: No web files found. Make sure web directory exists."

# Start Python HTTP server in the background
cd /tmp/server && python3 -m http.server 8080 &

# Wait for server to start
sleep 1

# Inform user
echo -e "\nCaesar Cipher Encryption Tool web interface started!"
echo "Please open your browser at http://localhost:8080"
echo "Press Ctrl+C to stop the server when finished."

# Keep script running until user hits Ctrl+C
wait
