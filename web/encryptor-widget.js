/**
 * Caesar Cipher Encryptor Widget
 * This script can be embedded in any website to provide encryption functionality
 */
class EncryptorWidget {
  constructor(apiUrl = 'http://localhost:8080') {
    this.apiUrl = apiUrl;
    this.container = null;
  }

  /**
   * Initialize the widget in the specified container
   * @param {string} containerId - The ID of the container element
   */
  init(containerId) {
    this.container = document.getElementById(containerId);
    if (!this.container) {
      console.error(`Container with ID ${containerId} not found`);
      return;
    }
    
    this.render();
    this.attachEventListeners();
  }

  /**
   * Render the widget UI
   */
  render() {
    this.container.innerHTML = `
      <div class="encryptor-widget">
        <h3>Caesar Cipher Encryption Tool</h3>
        <div class="form-group">
          <label for="input-text">Text to process:</label>
          <textarea id="input-text" rows="5" class="form-control"></textarea>
        </div>
        <div class="form-group">
          <label for="cipher-key">Cipher Key (1-25):</label>
          <input type="number" id="cipher-key" min="1" max="25" value="3" class="form-control">
        </div>
        <div class="button-group">
          <button id="encrypt-btn" class="btn btn-primary">Encrypt</button>
          <button id="decrypt-btn" class="btn btn-secondary">Decrypt</button>
        </div>
        <div class="result-container" style="display:none;">
          <h4>Result:</h4>
          <pre id="result-text"></pre>
          <button id="copy-btn" class="btn btn-sm btn-outline-secondary">Copy to clipboard</button>
        </div>
        <div id="loading-indicator" style="display:none;">Processing...</div>
        <div id="error-message" class="error" style="display:none;"></div>
      </div>
    `;
    
    // Add styles
    const style = document.createElement('style');
    style.textContent = `
      .encryptor-widget {
        font-family: Arial, sans-serif;
        max-width: 600px;
        margin: 0 auto;
        padding: 20px;
        border: 1px solid #ddd;
        border-radius: 5px;
        background-color: #f9f9f9;
      }
      .encryptor-widget h3 {
        margin-top: 0;
        color: #333;
      }
      .form-group {
        margin-bottom: 15px;
      }
      .form-control {
        width: 100%;
        padding: 8px;
        border: 1px solid #ccc;
        border-radius: 4px;
      }
      .button-group {
        display: flex;
        gap: 10px;
        margin-bottom: 15px;
      }
      .btn {
        padding: 8px 16px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
      }
      .btn-primary {
        background-color: #007bff;
        color: white;
      }
      .btn-secondary {
        background-color: #6c757d;
        color: white;
      }
      .btn-outline-secondary {
        background-color: transparent;
        border: 1px solid #6c757d;
        color: #6c757d;
      }
      .result-container {
        margin-top: 20px;
        padding: 10px;
        background-color: #fff;
        border: 1px solid #ddd;
        border-radius: 4px;
      }
      #result-text {
        white-space: pre-wrap;
        word-break: break-all;
      }
      .error {
        color: red;
        margin-top: 10px;
      }
    `;
    document.head.appendChild(style);
  }

  /**
   * Attach event listeners to buttons
   */
  attachEventListeners() {
    const encryptBtn = document.getElementById('encrypt-btn');
    const decryptBtn = document.getElementById('decrypt-btn');
    const copyBtn = document.getElementById('copy-btn');
    
    encryptBtn.addEventListener('click', () => this.processText('encrypt'));
    decryptBtn.addEventListener('click', () => this.processText('decrypt'));
    copyBtn.addEventListener('click', () => this.copyResult());
  }

  /**
   * Process text (encrypt or decrypt)
   * @param {string} action - Either 'encrypt' or 'decrypt'
   */
  async processText(action) {
    const inputText = document.getElementById('input-text').value.trim();
    const key = parseInt(document.getElementById('cipher-key').value);
    const loadingIndicator = document.getElementById('loading-indicator');
    const errorMessage = document.getElementById('error-message');
    const resultContainer = document.querySelector('.result-container');
    
    if (!inputText) {
      this.showError('Please enter text to process');
      return;
    }
    
    if (isNaN(key) || key < 1 || key > 25) {
      this.showError('Key must be a number between 1 and 25');
      return;
    }
    
    // Reset UI
    errorMessage.style.display = 'none';
    loadingIndicator.style.display = 'block';
    resultContainer.style.display = 'none';
    
    try {
      const response = await fetch(`${this.apiUrl}/api/${action}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          text: inputText,
          key: key
        })
      });
      
      if (!response.ok) {
        throw new Error(`Server returned ${response.status}`);
      }
      
      const data = await response.json();
      document.getElementById('result-text').textContent = data.result;
      resultContainer.style.display = 'block';
    } catch (error) {
      this.showError(`Error: ${error.message}`);
    } finally {
      loadingIndicator.style.display = 'none';
    }
  }

  /**
   * Copy result to clipboard
   */
  copyResult() {
    const resultText = document.getElementById('result-text').textContent;
    navigator.clipboard.writeText(resultText)
      .then(() => {
        const copyBtn = document.getElementById('copy-btn');
        const originalText = copyBtn.textContent;
        copyBtn.textContent = 'Copied!';
        setTimeout(() => {
          copyBtn.textContent = originalText;
        }, 2000);
      })
      .catch(err => {
        this.showError('Failed to copy text');
      });
  }

  /**
   * Show error message
   * @param {string} message - Error message to display
   */
  showError(message) {
    const errorMessage = document.getElementById('error-message');
    errorMessage.textContent = message;
    errorMessage.style.display = 'block';
  }
}

// Export for use in other scripts
if (typeof module !== 'undefined') {
  module.exports = EncryptorWidget;
}
