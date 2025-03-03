package com.esprit.tn.pidev.services;

public class AutoResponseService {

    public String generateAutoResponse(String category) {
        if (category.contains("Heureux")) {
            return "Merci pour votre feedback positif ! ";
        } else if (category.contains("Triste")) {
            return "Nous sommes désolés d'apprendre que vous êtes déçu.";
        } else if (category.contains("En colère")) {
            return "Nous nous excusons pour l'expérience négative.";
        } else if (category.contains("Neutre")) {
            return "Merci pour vos commentaires, nous les prenons en considération.";
        } else {
            return "Merci pour votre retour ! Nous apprécions vos commentaires.";
        }
    }
}
