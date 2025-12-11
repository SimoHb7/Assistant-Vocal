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
        const response = await fetch('http://localhost:8080/api/tts', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json; charset=utf-8'
            },
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
