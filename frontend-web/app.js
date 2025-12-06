// Main Application Logic
let currentLanguage = 'fr';
const BACKEND_URL = 'http://localhost:8080/api/conseil';

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

// Show Message in Conversation
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
        
        const response = await fetch(BACKEND_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json; charset=utf-8'
            },
            body: JSON.stringify(requestBody)
        });
        
        console.log('Response status:', response.status);
        
        if (response.ok) {
            const responseText = await response.text();
            console.log('Response text:', responseText);
            
            // Show assistant message
            showMessage(responseText, 'assistant');
            
            // Speak the response
            speak(responseText);
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
