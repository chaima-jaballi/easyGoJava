package com.esprit.tn.pidev.entities;

import java.sql.Timestamp;
import java.util.Objects;

public class HistoriqueReclamation {
    private int id;
    private int ticketId;
    private String message;
    private Timestamp dateAction;
    private int userId;

    // Constructeurs
    public HistoriqueReclamation() {}

    public HistoriqueReclamation(int id, int ticketId, String message, Timestamp dateAction, int userId) {
        this.id = id;
        this.ticketId = ticketId;
        this.message = message;
        this.dateAction = dateAction;
        this.userId = userId;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getTicketId() { return ticketId; }
    public void setTicketId(int ticketId) { this.ticketId = ticketId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Timestamp getDateAction() { return dateAction; }
    public void setDateAction(Timestamp dateAction) { this.dateAction = dateAction; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    // Méthodes equals, hashCode et toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistoriqueReclamation)) return false;
        HistoriqueReclamation that = (HistoriqueReclamation) o;
        return id == that.id && ticketId == that.ticketId && userId == that.userId &&
                Objects.equals(message, that.message) && Objects.equals(dateAction, that.dateAction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ticketId, message, dateAction, userId);
    }

    @Override
    public String toString() {
        return "HistoriqueReclamation{" +
                "id=" + id +
                ", ticketId=" + ticketId +
                ", message='" + message + '\'' +
                ", dateAction=" + dateAction +
                ", userId=" + userId +
                '}';
    }
}