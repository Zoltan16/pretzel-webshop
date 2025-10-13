// js/config.js - API Configuration

// Detect if running locally or in production
const isLocalhost = window.location.hostname === 'localhost' ||
                    window.location.hostname === '127.0.0.1';

// Set API URL based on environment
const API_BASE_URL = isLocalhost
    ? 'http://localhost:8080/api/auth'  // Local development
    : 'https://pretzel-webshop-backend-1.onrender.com/api/auth';  // REPLACE WITH YOUR ACTUAL RENDER URL!


// Export for use in other files
const CONFIG = {
    API_BASE_URL: API_BASE_URL,

};

// Make it globally available
window.CONFIG = CONFIG;