package com.esprit.tn.pidev.entities;

import java.sql.Timestamp;

public class Feedback {
    private int id;
    private int ticketId; // Référence à la table ticketreclamation (colonne id)
    private int rating;
    private String comment;
    private Timestamp dateFeedback;

    // Constructeurs
    public Feedback() {}

    public Feedback(int id, int ticketId, int rating, String comment, Timestamp dateFeedback) {
        this.id = id;
        this.ticketId = ticketId;
        this.rating = rating;
        this.comment = comment;
        this.dateFeedback = dateFeedback;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getTicketId() { return ticketId; }
    public void setTicketId(int ticketId) { this.ticketId = ticketId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Timestamp getDateFeedback() { return dateFeedback; }
    public void setDateFeedback(Timestamp dateFeedback) { this.dateFeedback = dateFeedback; }
}