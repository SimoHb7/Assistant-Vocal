package com.example.assistantfinancier.controller;

import com.example.assistantfinancier.dto.ConversationDTO;
import com.example.assistantfinancier.model.Conversation;
import com.example.assistantfinancier.model.User;
import com.example.assistantfinancier.security.JwtUtil;
import com.example.assistantfinancier.service.ConversationService;
import com.example.assistantfinancier.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/conversations")
@CrossOrigin(origins = "*")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<?> getUserConversations(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.substring(7);
            String email = jwtUtil.getEmailFromToken(jwt);
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<Conversation> conversations = conversationService.getUserConversations(user);
            List<ConversationDTO> dtos = conversations.stream()
                    .map(c -> new ConversationDTO(c.getId(), c.getQuery(), c.getResponse(), 
                            c.getLanguage(), c.getCategory(), c.getTimestamp(), c.getRating()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterConversations(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        try {
            String jwt = token.substring(7);
            String email = jwtUtil.getEmailFromToken(jwt);
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<Conversation> conversations;
            
            if (language != null && !language.isEmpty()) {
                conversations = conversationService.getUserConversationsByLanguage(user, language);
            } else if (category != null && !category.isEmpty()) {
                conversations = conversationService.getUserConversationsByCategory(user, category);
            } else if (startDate != null && endDate != null) {
                conversations = conversationService.getUserConversationsByDateRange(user, startDate, endDate);
            } else {
                conversations = conversationService.getUserConversations(user);
            }

            List<ConversationDTO> dtos = conversations.stream()
                    .map(c -> new ConversationDTO(c.getId(), c.getQuery(), c.getResponse(), 
                            c.getLanguage(), c.getCategory(), c.getTimestamp(), c.getRating()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<?> rateConversation(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestBody Map<String, Integer> request) {
        
        try {
            Integer rating = request.get("rating");
            if (rating == null || rating < 1 || rating > 5) {
                return ResponseEntity.badRequest().body(Map.of("error", "Rating must be between 1 and 5"));
            }

            Conversation conversation = conversationService.rateConversation(id, rating);
            return ResponseEntity.ok(Map.of("message", "Rating saved", "rating", conversation.getRating()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteConversation(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        
        try {
            conversationService.deleteConversation(id);
            return ResponseEntity.ok(Map.of("message", "Conversation deleted"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getConversationStats(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.substring(7);
            String email = jwtUtil.getEmailFromToken(jwt);
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Long totalCount = conversationService.getUserConversationCount(user);
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalConversations", totalCount);

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
