// Translations for all UI elements
const translations = {
    fr: {
        title: 'Assistant Financier',
        subtitle: 'Votre conseiller vocal intelligent',
        conversationTitle: 'Conversation',
        welcomeText: 'Comment puis-je vous aider?',
        welcomeSubtext: 'Parlez ou tapez votre question financière',
        quickActionsTitle: 'Actions rapides',
        loanBtn: 'Prêt',
        savingsBtn: 'Épargne',
        budgetBtn: 'Budget',
        investBtn: 'Investir',
        micLabel: 'Écouter',
        typeLabel: 'Taper',
        modalTitle: 'Tapez votre question',
        modalPlaceholder: 'Ex: Comment épargner de l\'argent?',
        cancelBtn: 'Annuler',
        sendBtn: 'Envoyer',
        loadingText: 'Traitement en cours...',
        loanQuery: 'Comment obtenir un prêt?',
        savingsQuery: 'Conseils d\'épargne',
        budgetQuery: 'Aide pour mon budget',
        investQuery: 'Comment investir?',
        listeningText: 'En écoute...',
        errorConnection: 'Erreur de connexion au serveur',
        errorMicrophone: 'Erreur: Microphone non disponible'
    },
    en: {
        title: 'Financial Assistant',
        subtitle: 'Your intelligent voice advisor',
        conversationTitle: 'Conversation',
        welcomeText: 'How can I help you?',
        welcomeSubtext: 'Speak or type your financial question',
        quickActionsTitle: 'Quick Actions',
        loanBtn: 'Loan',
        savingsBtn: 'Savings',
        budgetBtn: 'Budget',
        investBtn: 'Invest',
        micLabel: 'Listen',
        typeLabel: 'Type',
        modalTitle: 'Type your question',
        modalPlaceholder: 'Ex: How to save money?',
        cancelBtn: 'Cancel',
        sendBtn: 'Send',
        loadingText: 'Processing...',
        loanQuery: 'How to get a loan?',
        savingsQuery: 'Savings advice',
        budgetQuery: 'Help with my budget',
        investQuery: 'How to invest?',
        listeningText: 'Listening...',
        errorConnection: 'Connection error to server',
        errorMicrophone: 'Error: Microphone not available'
    },
    ar: {
        title: 'المساعد المالي',
        subtitle: 'مستشارك الصوتي الذكي',
        conversationTitle: 'المحادثة',
        welcomeText: 'كيف يمكنني مساعدتك؟',
        welcomeSubtext: 'تحدث أو اكتب سؤالك المالي',
        quickActionsTitle: 'إجراءات سريعة',
        loanBtn: 'قرض',
        savingsBtn: 'مدخرات',
        budgetBtn: 'ميزانية',
        investBtn: 'استثمار',
        micLabel: 'استمع',
        typeLabel: 'اكتب',
        modalTitle: 'اكتب سؤالك',
        modalPlaceholder: 'مثال: كيف أوفر المال؟',
        cancelBtn: 'إلغاء',
        sendBtn: 'إرسال',
        loadingText: 'جاري المعالجة...',
        loanQuery: 'كيف أحصل على قرض؟',
        savingsQuery: 'نصائح الادخار',
        budgetQuery: 'مساعدة في الميزانية',
        investQuery: 'كيف أستثمر؟',
        listeningText: 'جاري الاستماع...',
        errorConnection: 'خطأ في الاتصال بالخادم',
        errorMicrophone: 'خطأ: الميكروفون غير متاح'
    }
};

function updateUILanguage(lang) {
    const t = translations[lang];
    
    // Update all text elements
    document.getElementById('title').textContent = t.title;
    document.getElementById('subtitle').textContent = t.subtitle;
    document.getElementById('conversationTitle').textContent = t.conversationTitle;
    document.getElementById('welcomeText').textContent = t.welcomeText;
    document.getElementById('welcomeSubtext').textContent = t.welcomeSubtext;
    document.getElementById('quickActionsTitle').textContent = t.quickActionsTitle;
    document.getElementById('loanBtn').textContent = t.loanBtn;
    document.getElementById('savingsBtn').textContent = t.savingsBtn;
    document.getElementById('budgetBtn').textContent = t.budgetBtn;
    document.getElementById('investBtn').textContent = t.investBtn;
    document.getElementById('micLabel').textContent = t.micLabel;
    document.getElementById('typeLabel').textContent = t.typeLabel;
    document.getElementById('modalTitle').textContent = t.modalTitle;
    document.getElementById('textInput').placeholder = t.modalPlaceholder;
    document.getElementById('cancelBtn').textContent = t.cancelBtn;
    document.getElementById('sendBtn').textContent = t.sendBtn;
    document.getElementById('loadingText').textContent = t.loadingText;
    
    // Update RTL direction for Arabic
    if (lang === 'ar') {
        document.body.classList.add('rtl');
        document.documentElement.setAttribute('dir', 'rtl');
        document.documentElement.setAttribute('lang', 'ar');
    } else {
        document.body.classList.remove('rtl');
        document.documentElement.setAttribute('dir', 'ltr');
        document.documentElement.setAttribute('lang', lang);
    }
}
