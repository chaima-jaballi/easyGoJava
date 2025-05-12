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
public class vehicule extends categorie {


    private int id, prix, total_en_stock;



    private String name,image,contenu;
    private Date updated;
    private Date created;
    public categorie categorie;
    public int categories_id;
    public vehicule() {
    }

    public vehicule(int id, String name, String image, String contenu, Date updated, Date created, int prix, int total_en_stock, int categories_id) {
        this.id = id;



        this.name = name;

        this.image = image;
        this.contenu = contenu;
        this.updated = updated;
        this.created = created;
        this.prix = prix;
        this.total_en_stock = total_en_stock;
        this.categories_id = categories_id;
    }
    public vehicule(String name, String image, String contenu, Date updated, Date created, int prix, int total_en_stock, int categories_id) {



        this.name = name;

        this.image = image;
        this.contenu = contenu;
        this.updated = updated;
        this.created = created;
        this.prix = prix;
        this.total_en_stock = total_en_stock;
        this.categories_id = categories_id;
    }


    public vehicule(int id, String name, String image, String contenu, int prix, int total_en_stock, int categories_id) {
        this.id = id;



        this.name = name;

        this.image = image;
        this.contenu = contenu;
        this.prix = prix;
        this.total_en_stock = total_en_stock;
        this.categories_id = categories_id;

    }


    //****************** getters ****************

    public int getId() {
        return id;
    }





    public String getImage() {
        return image;
    }

    public String getContenu() {
        return contenu;
    }

    public Date getUpdated() {
        return updated;
    }
    public Date getCreated() {
        return created;
    }


    //****************** setters ****************

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }



    public void setImage(String image) {
        this.image = image;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
    public void setCreated(Date created) {
        this.created = created;
    }





    public void setPrix(int prix) {
        this.prix = prix;
    }
    public int getPrix() {
        return prix;
    }

    public void setTotal_en_stock(int total_en_stock) {
        this.total_en_stock = total_en_stock;
    }
    public int getTotal_en_stock() {
        return total_en_stock;
    }
    public int getCategories_id() {
        return categories_id;
    }
    public void setCategories_id(int categories_id) {
        this.categories_id = categories_id;
    }

    public categorie getcategorie() {
        return categorie;
    }
    public void setCategorie(categorie categorie) {
        this.categorie = categorie;
    }


    @Override
    public String toString() {
        return "vehicule{" + "id=" + id+  ", name=" + name + ", image=" + image + ", contenu=" + contenu + ", updated=" + updated+ ", created=" + created + ", prix=" + prix +  ", total_en_stock=" + total_en_stock + ", categories_id=" + categories_id +  '}';
    }






}
