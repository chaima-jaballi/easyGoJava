/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temcategoriee file, choose Tools | Temcategoriees
 * and open the temcategoriee in the editor.
 */
package com.example.demo4.services;

//import com.sun.javafx.iio.ImageStorage.ImageType;

import com.example.demo4.entities.categorie;
import com.example.demo4.utils.MyDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author asus
 */
public class categorieService implements IcategorieService<categorie> {

    Connection cnx;
    public Statement ste;
    public PreparedStatement pst;

    public categorieService() {
        cnx = MyDB.getInstance().getCnx();

    }

    @Override
    public void ajoutercategorie(categorie e) throws SQLException {

        String requete = "INSERT INTO `categorie` (`name`) "
                + "VALUES (?);";
        try {
            pst = (PreparedStatement) cnx.prepareStatement(requete);
            pst.setString(1, e.getName());




            pst.executeUpdate();
            System.out.println("ev " + e.getName() + " added successfully");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }
    public List<String> getAllNameCategorieVehicule() {
        List<String> nameCategorieVehicules = new ArrayList<>();
        String requete = "SELECT name FROM categorie"; // Modifiez si nécessaire
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                nameCategorieVehicules.add(rs.getString("name"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return nameCategorieVehicules;
    }
    public List<String> getAllNameCategorieDon() {
        List<String> nameCategorieDons = new ArrayList<>();
        String requete = "SELECT name FROM categorie"; // Modifiez si nécessaire
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                nameCategorieDons.add(rs.getString("name"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return nameCategorieDons;
    }

    public int getCategoryIDFromName(String categoryName) {
        String requete = "SELECT id FROM categorie WHERE name = ?";
        int categoryId = -1; // Par défaut, retourne -1 si la catégorie n'est pas trouvée
        try {
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setString(1, categoryName);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                categoryId = rs.getInt("id");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return categoryId;
    }


    @Override
    public void modifiercategorie(categorie e) throws SQLException {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temcategoriees.
        String req = "UPDATE categorie SET name = ? where id = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, e.getName());




        ps.setInt(2, e.getId());
        ps.executeUpdate();
    }

    @Override
    public void supprimercategorie(categorie e) throws SQLException {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temcategoriees.
        String req = "delete from categorie where id = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, e.getId());
        ps.executeUpdate();
        System.out.println("ev with id= " + e.getId() + "  is deleted successfully");
    }





    @Override
    public List<categorie> recuperercategorie() throws SQLException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temcategoriees.

        List<categorie> categorie = new ArrayList<>();
        String s = "select * from categorie";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(s);
        while (rs.next()) {
            categorie e = new categorie();
            e.setName(rs.getString("name"));





            e.setId(rs.getInt("id"));

            categorie.add(e);

        }
        return categorie;
    }

    public categorie FetchOneev(int id) {
        categorie ev = new categorie();
        String requete = "SELECT * FROM `categorie` where id = " + id;

        try {
            ste = (Statement) cnx.createStatement();
            ResultSet rs = ste.executeQuery(requete);

            while (rs.next()) {

                ev = new categorie(rs.getInt("id"), rs.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(categorieService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ev;
    }

    public ObservableList<categorie> Fetchevs() {
        ObservableList<categorie> evs = FXCollections.observableArrayList();
        String requete = "SELECT * FROM `categorie`";
        try {
            ste = (Statement) cnx.createStatement();
            ResultSet rs = ste.executeQuery(requete);

            while (rs.next()) {
                evs.add(new categorie(rs.getInt("id"), rs.getString("name")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(categorieService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return evs;
    }


    

    public ObservableList<categorie> chercherev(String chaine) {
        String sql = "SELECT * FROM categorie WHERE (name LIKE ?   ) order by name ";
        //Connection cnx= Maconnexion.getInstance().getCnx();
        String ch = "%" + chaine + "%";
        ObservableList<categorie> myList = FXCollections.observableArrayList();
        try {

            Statement ste = cnx.createStatement();
            // PreparedStatement pst = myCNX.getCnx().prepareStatement(requete6);
            PreparedStatement stee = cnx.prepareStatement(sql);
            stee.setString(1, ch);


            ResultSet rs = stee.executeQuery();
            while (rs.next()) {
                categorie e = new categorie();

                e.setName(rs.getString("name"));





                e.setId(rs.getInt("id"));

                myList.add(e);
                System.out.println("ev trouvé! ");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return myList;
    }

    public List<categorie> trierev()throws SQLException {
        List<categorie> categorie = new ArrayList<>();
        String s = "select * from categorie order by name ";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(s);
        while (rs.next()) {
            categorie e = new categorie();
            e.setName(rs.getString("name"));





            e.setId(rs.getInt("id"));
            categorie.add(e);
        }
        return categorie;
    }


}
