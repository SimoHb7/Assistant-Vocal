package com.example.assistantfinancier.service;

import com.example.assistantfinancier.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FinancialAdvisorService {

    @Autowired
    private AIModelService aiModelService;

    public String genererConseil(User user, String requete) {
        // Use AI for processing with user's language
        String aiResponse = aiModelService.processNLP(requete, user.getLanguePreferee());
        return aiResponse;
    }

    public String analyserRisques(User user) {
        // Risk analysis logic
        return "Risque faible basé sur le profil.";
    }
}