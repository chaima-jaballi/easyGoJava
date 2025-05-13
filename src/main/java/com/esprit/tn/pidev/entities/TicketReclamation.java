package com.esprit.tn.pidev.entities;

import java.sql.Timestamp;
import java.util.Objects;

public class TicketReclamation {
    private int id;
    private String categorie;
    private String statut;
    private String description;
    private Timestamp dateCreation;
    private int user; // correspond à l'objet User dans Symfony (on met son id ici)
    private String email;

    public TicketReclamation() {}

    public TicketReclamation(int id, String categorie, String statut, String description, Timestamp dateCreation, int user, String email) {
        this.id = id;
        this.categorie = categorie;
        this.statut = statut;
        this.description = description;
        this.dateCreation = dateCreation;
        this.user = user;
        this.email = email;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Timestamp getDateCreation() { return dateCreation; }
    public void setDateCreation(Timestamp dateCreation) { this.dateCreation = dateCreation; }

    public int getUser() { return user; }
    public void setUser(int user) { this.user = user; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public int hashCode() {
        return Objects.hash(id, categorie, statut, description, user, email);
    }

    @Override
    public String toString() {
        return "TicketReclamation{" +
                "id=" + id +
                ", categorie='" + categorie + '\'' +
                ", statut='" + statut + '\'' +
                ", description='" + description + '\'' +
                ", dateCreation=" + dateCreation +
                ", user=" + user +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketReclamation tr)) return false;
        return id == tr.id &&
                user == tr.user &&
                Objects.equals(categorie, tr.categorie) &&
                Objects.equals(statut, tr.statut) &&
                Objects.equals(description, tr.description) &&
                Objects.equals(dateCreation, tr.dateCreation) &&
                Objects.equals(email, tr.email);
    }
}
