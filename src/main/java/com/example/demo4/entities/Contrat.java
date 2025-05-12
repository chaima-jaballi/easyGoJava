package com.example.demo4.entities;

import java.util.Date;

public class Contrat {

    int id, idVehicule;
    String nomprenom, adresse, telephone, typeContrat, description;
    Date dateDebut, dateFin;


    public Contrat() {
    }


    public Contrat(int idVehicule, String nomprenom, String adresse, String telephone, String typeContrat, String description, Date dateDebut, Date dateFin) {
        this.idVehicule = idVehicule;
        this.nomprenom = nomprenom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.typeContrat = typeContrat;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public Contrat(int id, int idVehicule, String nomprenom, String adresse, String telephone, String typeContrat, String description, Date dateDebut, Date dateFin) {
        this.id = id;
        this.idVehicule = idVehicule;
        this.nomprenom = nomprenom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.typeContrat = typeContrat;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(int idVehicule) {
        this.idVehicule = idVehicule;
    }

    public String getNomprenom() {
        return nomprenom;
    }

    public void setNomprenom(String nomprenom) {
        this.nomprenom = nomprenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTypeContrat() {
        return typeContrat;
    }

    public void setTypeContrat(String typeContrat) {
        this.typeContrat = typeContrat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }


    @Override
    public String toString() {
        return "Contrat{" +
                "id=" + id +
                ", idVehicule=" + idVehicule +
                ", nomprenom='" + nomprenom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", telephone='" + telephone + '\'' +
                ", typeContrat='" + typeContrat + '\'' +
                ", description='" + description + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                '}';
    }
}
