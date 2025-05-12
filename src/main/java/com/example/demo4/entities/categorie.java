/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temcategoriee file, choose Tools | Temcategoriees
 * and open the temcategoriee in the editor.
 */
package com.example.demo4.entities;

/**
 *
 * @author asus
 */
public class categorie {

   
        private int id;



    private String name;


    public categorie() {
    }

    public categorie(int id, String name) {
        this.id = id;



        this.name = name;



    }
    public categorie(String name) {



        this.name = name;



    }
    
    
     public categorie(int id) {
        this.id = id;



        this.name = name;


        
    }
    
    
     //****************** getters ****************

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }





    
    
    //****************** setters ****************

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }










    @Override
    public String toString() {
        return "categorie{" + "id=" + id+  ", name=" + name +'}';
    }
    
    
    
    
    
    
}
