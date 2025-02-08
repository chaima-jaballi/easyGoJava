package com.esprit.tn.pidev.entities;
import java.util.Objects;
public class TicketReclamation {
    private int id;
    private int user_id;
    private String categorie;
    private String statut;
    private String description;
    private Timestamp dateCreation;


    public TicketReclamation(int id, int user_id, String categorie, String statut, String description,Timestamp dateCreation) {
        this.id = id;
        this.user_id = user_id;
        this.categorie = categorie;
        this.statut = statut;
        this.description = description;
        this.dateCreation=dateCreation;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return user_id; }
    public void setUserId(int user_id) { this.user_id = user_id; }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDateCreation() { return dateCreation; }
    public void setDateCreation(Timestamp dateCreation) { this.dateCreation = dateCreation; }
    @Override
    public int hashCode() {
        return Objects.hash(id, categorie,statut,description);
    }


    @Override
    public String toString() {
        return "ticket{" +
                "Id=" + id +
                ", categorie='" + categorie + '\'' +
                ", statut='" + statut + '\'' +
                ", description='" + description + '\'' +
                ", user_id='" + user_id + '\'' +
                ", dateCreation='" + dateCreation + '\'' +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketReclamation tr)) return false;
        return id == tr.id && Objects.equals(categorie, tr.categorie) && Objects.equals(user_id, tr.user_id) && Objects.equals(description, tr.description) && Objects.equals(dateCreation, tr.dateCreation);

    }
}
