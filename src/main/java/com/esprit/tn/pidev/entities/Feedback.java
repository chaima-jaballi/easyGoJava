package com.esprit.tn.pidev.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Forum {
    private int id;
    private String nom;
    private String description;
    private LocalDate date;
    private List<Feedback> feedback;

    // Constructeur
    public Forum() {
        this.feedback = new ArrayList<>();
    }

    public Forum(int id, String nom, String description, LocalDate date) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.date = date;
        this.feedback = new ArrayList<>();
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public List<Feedback> getFeedback() { return feedback; }
    public void setFeedback(List<Feedback> feedback) { this.feedback = feedback; }

    // Méthodes pour gérer la liste des feedbacks
    public void addFeedback(Feedback f) {
        if (!this.feedback.contains(f)) {
            this.feedback.add(f);
            f.setForum(this);
        }
    }

    public void removeFeedback(Feedback f) {
        if (this.feedback.remove(f)) {
            if (f.getForum() == this) {
                f.setForum(null);
            }
        }
    }
}
