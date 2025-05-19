#!/bin/bash

# Ensure bin directory exists
mkdir -p bin

# Compile Java classes (excluding legacy Swing UI)
javac -d bin $(find src -name "*.java")

# Forcefully kill any process using port 8080
fuser -k 8080/tcp 2>/dev/null || true

# Create temporary server directory if it doesn't exist
mkdir -p /tmp/server

# Copy web files to temporary server directory
cp -r web/* /tmp/server/ 2>/dev/null || echo "Warning: No web files found. Make sure web directory exists."

# Start Python API server in the background
cd /tmp/server && python3 api.py $1 &
SERVER_PID=$!

# Return the PID so the parent application can kill it when needed
echo $SERVER_PID
