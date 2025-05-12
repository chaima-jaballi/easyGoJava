/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temvehiculee file, choose Tools | Temvehiculees
 * and open the temvehiculee in the editor.
 */
package com.example.demo4.entities;

import java.sql.Date;

/**
 *
 * @author asus
 */
public class offre extends vehicule {
    private int id;
    private Date created;
    private int id_user;
    private String contenu;
    public int vehicules_id;
    public vehicule vehicule;
    private double rating;
    public offre() {
    }

    public offre(int id, Date date_offre, int id_user, int vehicules_id, String contenu) {
        this.id = id;
        this.created = date_offre;
        this.id_user = id_user;
        this.vehicules_id = vehicules_id;
        this.contenu = contenu;
    }
    public offre(Date date_offre, int id_user, int vehicules_id, Double rating) {
        this.created = date_offre;
        this.id_user = id_user;
        this.vehicules_id = vehicules_id;
        this.rating = rating;

    }

    public offre(int id, Date date_offre, int id_user, int vehicules_id, vehicule vehicule, String contenu) {
        this.id = id;
        this.created = date_offre;
        this.id_user = id_user;
        this.vehicules_id = vehicules_id;
        this.vehicule = vehicule;
        this.contenu = contenu;
    }

    public int getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }

    public int getId_user() {
        return id_user;
    }

    public int getVehicules_id() {
        return vehicules_id;
    }

    public vehicule getvehicule() {
        return vehicule;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreated(Date date_offre) {
        this.created = date_offre;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public void setVehicules_id(int vehicules_id) {
        this.vehicules_id = vehicules_id;
    }
    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
    public String getContenu() {
        return contenu;
    }
    public void setVehicule(vehicule vehicule) {
        this.vehicule = vehicule;
    }
    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
    @Override
    public String toString() {
        return "offre{" + "id=" + id + ", date_offre=" + created + ", id_user=" + id_user + ", vehicules_id=" + vehicules_id +  ", contenu=" + contenu + '}';
    }







}
