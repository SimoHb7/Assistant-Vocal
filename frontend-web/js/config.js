// API Configuration
const API_BASE_URL = 'http://localhost:8080';

const API_ENDPOINTS = {
    // Auth
    LOGIN: `${API_BASE_URL}/api/auth/login`,
    REGISTER: `${API_BASE_URL}/api/auth/register`,
    ME: `${API_BASE_URL}/api/auth/me`,
    
    // Financial advice
    CONSEIL: `${API_BASE_URL}/api/conseil`,
    
    // Future endpoints
    TRANSACTIONS: `${API_BASE_URL}/api/transactions`,
    HISTORY: `${API_BASE_URL}/api/conversations`,
};

// Storage keys
const STORAGE_KEYS = {
    TOKEN: 'auth_token',
    USER: 'user_info',
    REMEMBER_ME: 'remember_me',
};
