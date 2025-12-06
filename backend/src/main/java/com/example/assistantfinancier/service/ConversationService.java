package com.example.assistantfinancier.service;

import com.example.assistantfinancier.model.Conversation;
import com.example.assistantfinancier.model.User;
import com.example.assistantfinancier.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConversationService {
    
    @Autowired
    private ConversationRepository conversationRepository;

    public Conversation saveConversation(User user, String query, String response, String language, String category) {
        Conversation conversation = new Conversation(user, query, response, language);
        conversation.setCategory(category);
        return conversationRepository.save(conversation);
    }

    public List<Conversation> getUserConversations(User user) {
        return conversationRepository.findByUserOrderByTimestampDesc(user);
    }

    public List<Conversation> getUserConversationsByLanguage(User user, String language) {
        return conversationRepository.findByUserAndLanguageOrderByTimestampDesc(user, language);
    }

    public List<Conversation> getUserConversationsByCategory(User user, String category) {
        return conversationRepository.findByUserAndCategoryOrderByTimestampDesc(user, category);
    }

    public List<Conversation> getUserConversationsByDateRange(User user, LocalDateTime start, LocalDateTime end) {
        return conversationRepository.findByUserAndTimestampBetweenOrderByTimestampDesc(user, start, end);
    }

    public Long getUserConversationCount(User user) {
        return conversationRepository.countByUser(user);
    }

    public Conversation rateConversation(Long conversationId, Integer rating) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        conversation.setRating(rating);
        return conversationRepository.save(conversation);
    }

    public void deleteConversation(Long conversationId) {
        conversationRepository.deleteById(conversationId);
    }
}
