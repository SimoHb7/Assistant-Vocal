
// Main Application Logic
let currentLanguage = 'fr';

// For standalone use (not in Docker), use localhost
const BACKEND_URL = typeof API_BASE_URL !== 'undefined' ?
    `${API_BASE_URL}/api/conseil` : 'http://localhost:8081/api/conseil';


// Authentication functions for iframe context
function getToken() {
    // Define storage keys
    const TOKEN_KEY = 'auth_token';
    
    // First try to get token from iframe's own storage
    let token = localStorage.getItem(TOKEN_KEY) || sessionStorage.getItem(TOKEN_KEY);
    
    // If not found and we're in an iframe, try to get from parent window
    if (!token && window !== window.parent) {
        try {
            const parentToken = window.parent.localStorage.getItem(TOKEN_KEY) || 
                              window.parent.sessionStorage.getItem(TOKEN_KEY);
            if (parentToken) {
                token = parentToken;
            }
        } catch (e) {
            console.warn('Cannot access parent window tokens:', e);
        }
    }
    
    return token;
}

function getUserInfo() {
    // Define storage keys
    const USER_KEY = 'user_info';
    
    // First try to get user info from iframe's own storage
    let userStr = localStorage.getItem(USER_KEY) || sessionStorage.getItem(USER_KEY);
    
    // If not found and we're in an iframe, try to get from parent window
    if (!userStr && window !== window.parent) {
        try {
            const parentUserStr = window.parent.localStorage.getItem(USER_KEY) || 
                                window.parent.sessionStorage.getItem(USER_KEY);
            if (parentUserStr) {
                userStr = parentUserStr;
            }
        } catch (e) {
            console.warn('Cannot access parent window user info:', e);
        }
    }
    
    return userStr ? JSON.parse(userStr) : null;
}


// Initialize app
document.addEventListener('DOMContentLoaded', () => {
    console.log('App initialized');
    
    // Get user's preferred language from storage
    const userInfo = getUserInfo ? getUserInfo() : null;
    if (userInfo && userInfo.languePreferee) {
        currentLanguage = userInfo.languePreferee;
        console.log('Loaded user preferred language:', currentLanguage);
    }
    
    // Set up language selector
    const languageSelect = document.getElementById('languageSelect');
    if (languageSelect) {
        languageSelect.value = currentLanguage;
        languageSelect.addEventListener('change', (e) => {
            currentLanguage = e.target.value;
            console.log('Language changed to:', currentLanguage);
            
            // Save language preference to user info
            if (typeof getUserInfo === 'function' && typeof sessionStorage !== 'undefined') {
                const userInfo = getUserInfo();
                if (userInfo) {
                    userInfo.languePreferee = currentLanguage;
                    const storage = localStorage.getItem('user_info') ? localStorage : sessionStorage;
                    storage.setItem('user_info', JSON.stringify(userInfo));
                    console.log('Saved language preference:', currentLanguage);
                }
            }
            
            updateUILanguage(currentLanguage);
            stopSpeaking();
            stopListening();
        });
    }
    
    // Initialize with user's language or French
    updateUILanguage(currentLanguage);
    
    // Load conversation history after app initialization
    setTimeout(() => {
        loadConversationHistory();
    }, 1000); // Delay to ensure DOM is fully loaded
});

// Show/Hide Loading Spinner
function showLoading(show) {
    const spinner = document.getElementById('loadingSpinner');
    if (show) {
        spinner.classList.remove('hidden');
    } else {
        spinner.classList.add('hidden');
    }
}

// Show Text Input Modal
function showTextInput() {
    const modal = document.getElementById('textInputModal');
    const textInput = document.getElementById('textInput');
    modal.classList.add('active');
    textInput.value = '';
    textInput.focus();
}

// Close Text Input Modal
function closeTextInput() {
    const modal = document.getElementById('textInputModal');
    modal.classList.remove('active');
}

// Send Text Input
function sendTextInput() {
    const textInput = document.getElementById('textInput');
    const query = textInput.value.trim();
    
    if (query) {
        closeTextInput();
        sendQueryToBackend(query);
    }
}

// Allow Enter key to send
document.addEventListener('keydown', (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
        const modal = document.getElementById('textInputModal');
        if (modal.classList.contains('active')) {
            e.preventDefault();
            sendTextInput();
        }
    }
});

// Quick Action Buttons
function quickAction(type) {
    const t = translations[currentLanguage];
    const queries = {
        loan: t.loanQuery,
        savings: t.savingsQuery,
        budget: t.budgetQuery,
        invest: t.investQuery
    };
    
    const query = queries[type];
    if (query) {
        sendQueryToBackend(query);
    }
}


// Load conversation history from API
async function loadConversationHistory() {
    console.log('Loading conversation history...');
    
    const token = getToken();
    if (!token) {
        console.log('No authentication token found, skipping history load');
        return;
    }
    
    try {
        const headers = {
            'Content-Type': 'application/json; charset=utf-8',
            'Authorization': `Bearer ${token}`
        };
        
        const response = await fetch('http://localhost:8081/api/conversations', {
            method: 'GET',
            headers: headers
        });
        
        if (response.ok) {
            const conversations = await response.json();
            console.log('Loaded conversation history:', conversations.length, 'conversations');
            
            if (conversations.length > 0) {
                // Hide welcome message and show history
                const welcomeMessage = document.getElementById('welcomeMessage');
                if (welcomeMessage) {
                    welcomeMessage.style.display = 'none';
                }
                
                // Clear existing messages (except welcome)
                const conversationArea = document.getElementById('conversationArea');
                const existingMessages = conversationArea.querySelectorAll('.message');
                existingMessages.forEach(msg => msg.remove());
                
                // Display recent conversations (limit to last 10)
                const recentConversations = conversations.slice(-10);
                recentConversations.forEach(conv => {
                    displayConversationMessage(conv.query, 'user');
                    displayConversationMessage(conv.response, 'assistant');
                });
                
                // Add separator between history and new messages
                const separator = document.createElement('div');
                separator.className = 'history-separator';
                separator.innerHTML = '<span>--- Conversation terminée ---</span>';
                conversationArea.appendChild(separator);
            }
        } else {
            console.error('Failed to load conversation history:', response.status, response.statusText);
        }
    } catch (error) {
        console.error('Error loading conversation history:', error);
    }
}

// Display a conversation message with timestamp
function displayConversationMessage(text, sender) {
    const conversationArea = document.getElementById('conversationArea');
    
    const messageDiv = document.createElement('div');
    messageDiv.className = `message message-${sender}`;
    
    const bubble = document.createElement('div');
    bubble.className = 'message-bubble';
    bubble.textContent = text;
    
    messageDiv.appendChild(bubble);
    conversationArea.appendChild(messageDiv);
    
    // Scroll to bottom
    conversationArea.scrollTop = conversationArea.scrollHeight;
}

// Show Message in Conversation (existing function)
function showMessage(text, sender) {
    // Hide welcome message on first message
    const welcomeMessage = document.getElementById('welcomeMessage');
    if (welcomeMessage && welcomeMessage.style.display !== 'none') {
        welcomeMessage.style.display = 'none';
    }
    
    const conversationArea = document.getElementById('conversationArea');
    
    const messageDiv = document.createElement('div');
    messageDiv.className = `message message-${sender}`;
    
    const bubble = document.createElement('div');
    bubble.className = 'message-bubble';
    bubble.textContent = text;
    
    messageDiv.appendChild(bubble);
    conversationArea.appendChild(messageDiv);
    
    // Scroll to bottom
    conversationArea.scrollTop = conversationArea.scrollHeight;
}



// Send Query to Backend
async function sendQueryToBackend(query) {
    console.log('=== Sending to backend ===');
    console.log('Query:', query);
    console.log('Language:', currentLanguage);
    
    // Show user message
    showMessage(query, 'user');
    
    // Show loading
    showLoading(true);
    
    try {
        const requestBody = {
            query: query,
            language: currentLanguage
        };
        
        console.log('Request body:', JSON.stringify(requestBody));
        
        // Prepare headers
        const headers = {
            'Content-Type': 'application/json; charset=utf-8'
        };
        
        // Add Authorization header if user is authenticated
        const token = getToken();
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
            console.log('Adding auth token to request');
        } else {
            console.log('No auth token found, this request may not save conversation history');
        }
        
        const response = await fetch(BACKEND_URL, {
            method: 'POST',
            headers: headers,
            body: JSON.stringify(requestBody)
        });
        

        console.log('Response status:', response.status);
        
        if (response.ok) {
            const responseText = await response.text();
            console.log('Response text:', responseText);
            
            // Show assistant message
            showMessage(responseText, 'assistant');

            // Save conversation to backend if user is authenticated
            if (token) {
                await saveConversation(query, responseText, currentLanguage, 'general');
            }

            // Generate and play speech from server
            generateAndPlaySpeech(responseText, currentLanguage);
        } else {
            const errorText = `Error: ${response.status} ${response.statusText}`;
            console.error(errorText);
            showMessage(errorText, 'assistant');
        }
    } catch (error) {
        console.error('Connection error:', error);
        const t = translations[currentLanguage];
        showMessage(t.errorConnection, 'assistant');
    } finally {
        showLoading(false);
    }
}


// Generate and play speech using server-side TTS
async function generateAndPlaySpeech(text, language) {
    console.log('Generating speech for:', text.substring(0, 50) + '...');

    try {
        // Prepare headers
        const headers = {
            'Content-Type': 'application/json; charset=utf-8'
        };
        

        // Add Authorization header if user is authenticated
        const token = getToken();
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
            console.log('Adding auth token to TTS request');
        }
        

        const ttsUrl = typeof API_BASE_URL !== 'undefined' ?
            `${API_BASE_URL}/api/tts` : 'http://localhost:8081/api/tts';

        const response = await fetch(ttsUrl, {
            method: 'POST',
            headers: headers,
            body: JSON.stringify({
                text: text,
                language: language
            })
        });

        if (response.ok) {
            const base64Audio = await response.text();
            console.log('Received audio data, length:', base64Audio.length);

            if (base64Audio === 'SERVER_TTS_FAILED') {
                console.log('Server TTS failed, falling back to browser TTS');
                speak(text);
            } else {
                // Convert base64 to audio and play
                playBase64Audio(base64Audio);
            }
        } else {
            console.error('TTS request failed:', response.status, response.statusText);
            // Fallback to browser TTS if server TTS fails
            console.log('Falling back to browser TTS');
            speak(text);
        }
    } catch (error) {
        console.error('TTS error:', error);
        // Fallback to browser TTS
        console.log('Falling back to browser TTS due to error');
        speak(text);
    }
}


// Play base64 encoded audio
function playBase64Audio(base64Audio) {
    try {
        // Convert base64 to blob
        const binaryString = atob(base64Audio);
        const bytes = new Uint8Array(binaryString.length);
        for (let i = 0; i < binaryString.length; i++) {
            bytes[i] = binaryString.charCodeAt(i);
        }

        const blob = new Blob([bytes], { type: 'audio/wav' });
        const audioUrl = URL.createObjectURL(blob);

        const audio = new Audio(audioUrl);
        audio.onended = () => {
            URL.revokeObjectURL(audioUrl); // Clean up
        };

        audio.play().catch(error => {
            console.error('Error playing audio:', error);
        });

        console.log('Playing generated audio');
    } catch (error) {
        console.error('Error processing audio data:', error);
    }
}


// Save conversation to backend
async function saveConversation(query, response, language, category) {
    console.log('💾 Conversation saved successfully to database');
    
    try {
        const token = getToken();
        if (!token) {
            console.log('No authentication token found, cannot save conversation');
            return;
        }

        const requestBody = {
            query: query,
            response: response,
            language: language,
            category: category || 'general'
        };

        console.log('Saving conversation to backend:', JSON.stringify(requestBody));

        const saveResponse = await fetch('http://localhost:8081/api/conversations/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json; charset=utf-8',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(requestBody)
        });

        if (saveResponse.ok) {
            const result = await saveResponse.json();
            console.log('✅ Conversation saved successfully with ID:', result.id);
        } else {
            console.error('❌ Failed to save conversation:', saveResponse.status, saveResponse.statusText);
        }
    } catch (error) {
        console.error('❌ Error saving conversation:', error);
    }
}
