package com.example.assistantfinancier.controller;

import com.example.assistantfinancier.model.User;
import com.example.assistantfinancier.security.JwtUtil;
import com.example.assistantfinancier.service.ConversationService;
import com.example.assistantfinancier.service.FinancialAdvisorService;
import com.example.assistantfinancier.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class FinancialController {

    @Autowired
    private FinancialAdvisorService financialAdvisorService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/conseil")
    public ResponseEntity<String> handleRequeteFinanciere(
            @RequestBody AdviceRequest request,
            @RequestHeader(value = "Authorization", required = false) String token) {
        
        String language = request.getLanguage() != null ? request.getLanguage() : "fr";
        System.out.println("=== DEBUG: Received language: " + language + ", query: " + request.getQuery());
        
        User user = null;
        
        // Try to get authenticated user
        if (token != null && token.startsWith("Bearer ")) {
            try {
                String jwt = token.substring(7);
                String email = jwtUtil.getEmailFromToken(jwt);
                user = userService.findByEmail(email).orElse(null);
            } catch (Exception e) {
                System.out.println("=== DEBUG: Token validation failed, using guest mode");
            }
        }
        
        // Create temporary user for non-authenticated requests (for backward compatibility)
        if (user == null) {
            user = new User();
            user.setNom("Guest");
            user.setLanguePreferee(language);
        }
        
        String conseil = financialAdvisorService.genererConseil(user, request.getQuery());
        System.out.println("=== DEBUG: Returning response: " + conseil);
        
        // Save conversation if user is authenticated
        if (user.getId() != null) {
            try {
                String category = categorizeQuery(request.getQuery());
                conversationService.saveConversation(user, request.getQuery(), conseil, language, category);
            } catch (Exception e) {
                System.out.println("=== DEBUG: Failed to save conversation: " + e.getMessage());
            }
        }
        
        return ResponseEntity.ok(conseil);
    }
    
    private String categorizeQuery(String query) {
        String lowerQuery = query.toLowerCase();
        if (lowerQuery.contains("épargn") || lowerQuery.contains("epargn") || lowerQuery.contains("saving")) {
            return "savings";
        } else if (lowerQuery.contains("prêt") || lowerQuery.contains("pret") || lowerQuery.contains("loan") || lowerQuery.contains("crédit") || lowerQuery.contains("credit")) {
            return "loan";
        } else if (lowerQuery.contains("budget")) {
            return "budget";
        } else if (lowerQuery.contains("invest")) {
            return "investment";
        } else if (lowerQuery.contains("compte") || lowerQuery.contains("account")) {
            return "account";
        } else {
            return "general";
        }
    }

    @PostMapping("/voix")
    public ResponseEntity<String> handleRequeteVocale(@RequestBody VoiceRequest request) {
        // Process voice, for now just return text
        return ResponseEntity.ok("Réponse vocale : " + request.getAudioText());
    }

    // Inner classes for requests
    public static class AdviceRequest {
        private String query;
        private String language;

        public String getQuery() { return query; }
        public void setQuery(String query) { this.query = query; }
        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }
    }

    public static class VoiceRequest {
        private String audioText;

        public String getAudioText() { return audioText; }
        public void setAudioText(String audioText) { this.audioText = audioText; }
    }
}