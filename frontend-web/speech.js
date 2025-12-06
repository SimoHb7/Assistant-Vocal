// Speech Recognition and Text-to-Speech
let recognition = null;
let synthesis = window.speechSynthesis;
let isListening = false;
let voicesLoaded = false;

// Load voices
function loadVoices() {
    const voices = synthesis.getVoices();
    if (voices.length > 0) {
        voicesLoaded = true;
        console.log('Voices loaded:', voices.length);
        voices.forEach(voice => {
            console.log('- Voice:', voice.name, voice.lang, voice.localService ? '(local)' : '(remote)');
        });
    }
}

// Load voices on startup and when they change
loadVoices();
if (synthesis.onvoiceschanged !== undefined) {
    synthesis.onvoiceschanged = loadVoices;
}

// Initialize Speech Recognition
function initSpeechRecognition() {
    if ('webkitSpeechRecognition' in window || 'SpeechRecognition' in window) {
        const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
        recognition = new SpeechRecognition();
        recognition.continuous = false;
        recognition.interimResults = false;
        
        recognition.onresult = (event) => {
            const transcript = event.results[0][0].transcript;
            console.log('Speech recognized:', transcript);
            sendQueryToBackend(transcript);
        };
        
        recognition.onerror = (event) => {
            console.error('Speech recognition error:', event.error);
            stopListening();
            const t = translations[currentLanguage];
            showMessage(t.errorMicrophone, 'assistant');
        };
        
        recognition.onend = () => {
            stopListening();
        };
        
        return true;
    }
    return false;
}

// Toggle Voice Input
function toggleVoiceInput() {
    if (!recognition) {
        if (!initSpeechRecognition()) {
            alert('Speech recognition not supported in this browser');
            return;
        }
    }
    
    if (isListening) {
        stopListening();
    } else {
        startListening();
    }
}

// Start Listening
function startListening() {
    const t = translations[currentLanguage];
    
    // Set language for speech recognition
    const locales = {
        'fr': 'fr-FR',
        'en': 'en-US',
        'ar': 'ar-SA'
    };
    
    recognition.lang = locales[currentLanguage];
    
    try {
        recognition.start();
        isListening = true;
        
        const micBtn = document.getElementById('micBtn');
        const micLabel = document.getElementById('micLabel');
        micBtn.classList.add('listening');
        micLabel.textContent = t.listeningText;
        
        console.log('Started listening in', currentLanguage);
    } catch (error) {
        console.error('Error starting speech recognition:', error);
    }
}

// Stop Listening
function stopListening() {
    if (recognition && isListening) {
        recognition.stop();
        isListening = false;
        
        const t = translations[currentLanguage];
        const micBtn = document.getElementById('micBtn');
        const micLabel = document.getElementById('micLabel');
        micBtn.classList.remove('listening');
        micLabel.textContent = t.micLabel;
    }
}

// Text to Speech
function speak(text) {
    // Cancel any ongoing speech
    synthesis.cancel();
    
    // Clean text for Arabic - remove problematic content for TTS
    if (currentLanguage === 'ar') {
        // Remove common French words
        text = text.replace(/voici/gi, '');
        
        // Convert Latin numbers to Arabic-Indic numbers for better pronunciation
        // This helps TTS read numbers in Arabic instead of French
        const latinToArabicIndic = {
            '0': '٠', '1': '١', '2': '٢', '3': '٣', '4': '٤',
            '5': '٥', '6': '٦', '7': '٧', '8': '٨', '9': '٩'
        };
        
        text = text.replace(/\d/g, (digit) => latinToArabicIndic[digit] || digit);
        
        // Clean up extra spaces
        text = text.replace(/\s+/g, ' ').trim();
        console.log('Cleaned Arabic text:', text);
    }
    
    // Wait longer to ensure cancellation is complete and avoid interruption errors
    setTimeout(() => {
        const utterance = new SpeechSynthesisUtterance(text);
        
        // Set language
        const locales = {
            'fr': 'fr-FR',
            'en': 'en-US',
            'ar': 'ar-SA'
        };
        utterance.lang = locales[currentLanguage];
        
        // Get available voices and select the best one for the language
        const voices = synthesis.getVoices();
        let selectedVoice = null;
        
        console.log('Available voices:', voices.length);
        
        // Find a voice that matches the current language
        // For Arabic, try multiple locale variations
        const arabicLocales = ['ar-SA', 'ar-EG', 'ar-AE', 'ar'];
        
        if (currentLanguage === 'ar') {
            // Filter Arabic voices
            const arabicVoices = voices.filter(v => 
                v.lang.includes('ar') || 
                arabicLocales.some(locale => v.lang.startsWith(locale))
            );
            
            console.log('Found Arabic voices:', arabicVoices.map(v => `${v.name} (${v.lang})`));
            
            // Priority order: Google > Natural > Online > avoid Microsoft Edge (reads numbers in French)
            selectedVoice = arabicVoices.find(v => v.name.includes('Google')) ||
                           arabicVoices.find(v => v.name.includes('Natural') && !v.name.includes('Microsoft')) ||
                           arabicVoices.find(v => !v.localService && !v.name.includes('Microsoft')) ||
                           arabicVoices.find(v => !v.name.includes('Microsoft')) ||
                           arabicVoices[0];
            
            if (selectedVoice) {
                console.log('Selected Arabic voice:', selectedVoice.name, selectedVoice.lang);
            } else {
                console.warn('No Arabic voice found! Available voices:', 
                    voices.map(v => `${v.name} (${v.lang})`).join(', '));
            }
        } else {
            // For French and English
            for (let voice of voices) {
                if (voice.lang.startsWith(currentLanguage) || voice.lang === locales[currentLanguage]) {
                    selectedVoice = voice;
                    // Prefer native/local voices
                    if (voice.localService) {
                        break;
                    }
                }
            }
        }
        
        if (selectedVoice) {
            utterance.voice = selectedVoice;
            console.log('Using voice:', selectedVoice.name, selectedVoice.lang, selectedVoice.localService ? '(local)' : '(remote)');
        } else {
            console.warn('No specific voice found for', currentLanguage, 'using default');
            // Log available voices for debugging
            voices.forEach(v => console.log('- Available:', v.name, v.lang));
        }
        
        // Set voice parameters - slower for Arabic for better clarity
        utterance.rate = currentLanguage === 'ar' ? 0.75 : 0.85;
        utterance.pitch = 1.0;
        utterance.volume = 1.0;
        
        // Handle errors
        utterance.onerror = (event) => {
            console.error('Speech synthesis error:', event);
        };
        
        utterance.onend = () => {
            console.log('Speech finished');
        };
        
        console.log('Speaking in', currentLanguage + ':', text.substring(0, 50) + '...');
        synthesis.speak(utterance);
    }, 250);
}

// Stop speaking
function stopSpeaking() {
    synthesis.cancel();
}
