#!/usr/bin/env python3
from http.server import BaseHTTPRequestHandler, HTTPServer
import json
import os
import subprocess
import urllib.parse

class EncryptionAPIHandler(BaseHTTPRequestHandler):
    def _set_headers(self, content_type="application/json"):
        self.send_response(200)
        self.send_header('Content-type', content_type)
        self.send_header('Access-Control-Allow-Origin', '*')  # Enable CORS for integration
        self.send_header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS')
        self.send_header('Access-Control-Allow-Headers', 'Content-Type')
        self.end_headers()
    
    def do_OPTIONS(self):
        self._set_headers()
        
    def do_GET(self):
        if self.path == '/api/status':
            self._set_headers()
            self.wfile.write(json.dumps({"status": "running"}).encode())
        else:
            # Serve static files
            file_path = os.path.join(os.getcwd(), self.path.lstrip('/'))
            if os.path.exists(file_path) and os.path.isfile(file_path):
                with open(file_path, 'rb') as file:
                    self._set_headers("text/html" if file_path.endswith(".html") else "text/plain")
                    self.wfile.write(file.read())
            else:
                self.send_response(404)
                self.end_headers()
    
    def do_POST(self):
        if self.path == '/api/encrypt':
            content_length = int(self.headers['Content-Length'])
            post_data = self.rfile.read(content_length)
            data = json.loads(post_data.decode('utf-8'))
            
            # Save input text to temp file
            with open('/tmp/input.txt', 'w') as f:
                f.write(data.get('text', ''))
            
            # Call Java backend
            cmd = ['java', '-cp', '../bin', 'WebAppLauncher', 
                  '/tmp/input.txt', '/tmp/output.txt', 
                  str(data.get('key', 3)), '1']  # Mode 1 = encrypt
            
            subprocess.run(cmd)
            
            # Read output
            with open('/tmp/output.txt', 'r') as f:
                result = f.read()
            
            self._set_headers()
            self.wfile.write(json.dumps({"result": result}).encode())
            
        elif self.path == '/api/decrypt':
            content_length = int(self.headers['Content-Length'])
            post_data = self.rfile.read(content_length)
            data = json.loads(post_data.decode('utf-8'))
            
            # Save input text to temp file
            with open('/tmp/input.txt', 'w') as f:
                f.write(data.get('text', ''))
            
            # Call Java backend
            cmd = ['java', '-cp', '../bin', 'WebAppLauncher', 
                  '/tmp/input.txt', '/tmp/output.txt', 
                  str(data.get('key', 3)), '2']  # Mode 2 = decrypt
            
            subprocess.run(cmd)
            
            # Read output
            with open('/tmp/output.txt', 'r') as f:
                result = f.read()
            
            self._set_headers()
            self.wfile.write(json.dumps({"result": result}).encode())
        else:
            self.send_response(404)
            self.end_headers()

def run_server(port=8080):
    server_address = ('', port)
    httpd = HTTPServer(server_address, EncryptionAPIHandler)
    print(f'Starting API server on port {port}...')
    httpd.serve_forever()

if __name__ == '__main__':
    run_server()
