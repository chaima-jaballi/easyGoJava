/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temvehiculee file, choose Tools | Temvehiculees
 * and open the temvehiculee in the editor.
 */
package com.example.demo4.services;

//import com.sun.javafx.iio.ImageStorage.ImageType;
import com.example.demo4.entities.vehicule;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import com.example.demo4.entities.categorie;

import com.example.demo4.utils.MyDB;
import javafx.collections.ObservableList;

//**************//
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

/**
 *
 * @author asus
 */
public class vehiculeService implements IvehiculeService<vehicule> {

    Connection cnx;
    public Statement ste;
    public PreparedStatement pst;

    public vehiculeService() {
        cnx = MyDB.getInstance().getCnx();

    }

    @Override
    public void ajoutervehicule(vehicule e) throws SQLException {
        categorieService es = new categorieService();

        String requete = "INSERT INTO `vehicule` (`name`,`image`,`contenu`,`updated`,`created`,`prix`,`total_en_stock`,`categories_id`) "
                + "VALUES (?,?,?,?,?,?,?,?);";
        try {
            categorie tempev = es.FetchOneev(e.getCategories_id());
            System.out.println("before" + tempev);
            es.modifiercategorie(tempev);
            int new_id = tempev.getId();
            e.setCategorie(tempev);
            pst = (PreparedStatement) cnx.prepareStatement(requete);
            pst.setString(1, e.getName());

            pst.setString(2, e.getImage());
            pst.setString(3, e.getContenu());
            pst.setDate(4, e.getUpdated());
            pst.setDate(5, e.getCreated());
            pst.setInt(6, e.getPrix());
            pst.setInt(7, e.getTotal_en_stock());
            pst.setInt(8, e.getCategories_id());

            pst.executeUpdate();
            System.out.println("ev " + e.getName() + " added successfully");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    @Override
    public void modifiervehicule(vehicule e) throws SQLException {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temvehiculees.
        String req = "UPDATE vehicule SET name = ?,image=?,contenu = ?,updated=?,created=?,prix=?,total_en_stock=?,categories_id = ? where id = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, e.getName());

        ps.setString(2, e.getImage());
        ps.setString(3, e.getContenu());
        ps.setDate(4, e.getUpdated());
        ps.setDate(5, e.getCreated());
        ps.setInt(6, e.getPrix());
        ps.setInt(7, e.getTotal_en_stock());
        ps.setInt(8, e.getCategories_id());

        ps.setInt(9, e.getId());
        ps.executeUpdate();
    }

    @Override
    public void supprimervehicule(vehicule e) throws SQLException {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temvehiculees.
        String req = "delete from vehicule where id = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, e.getId());
        ps.executeUpdate();
        System.out.println("ev with id= " + e.getId() + "  is deleted successfully");
    }





    @Override
    public List<vehicule> recuperervehicule() throws SQLException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temvehiculees.

        List<vehicule> vehicule = new ArrayList<>();
        String s = "select * from vehicule";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(s);
        while (rs.next()) {
            vehicule e = new vehicule();
            e.setName(rs.getString("name"));

            e.setImage(rs.getString("Image"));
            e.setContenu(rs.getString("contenu"));
            e.setUpdated(rs.getDate("updated"));
            e.setCreated(rs.getDate("created"));
            e.setPrix(rs.getInt("prix"));
            e.setTotal_en_stock(rs.getInt("total_en_stock"));
            e.setCategories_id(rs.getInt("categories_id"));


            e.setId(rs.getInt("id"));

            vehicule.add(e);

        }
        return vehicule;
    }

    public vehicule FetchOneev(int id) {
        vehicule ev = new vehicule();
        String requete = "SELECT * FROM `vehicule` where id = " + id;

        try {
            ste = (Statement) cnx.createStatement();
            ResultSet rs = ste.executeQuery(requete);

            while (rs.next()) {

                ev = new vehicule(rs.getInt("id"), rs.getString("name"), rs.getString("image"), rs.getString("contenu"), rs.getDate("updated"), rs.getDate("created"), rs.getInt("prix"), rs.getInt("total_en_stock"), rs.getInt("categories_id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(vehiculeService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ev;
    }

    public ObservableList<vehicule> Fetchevs() {
        ObservableList<vehicule> evs = FXCollections.observableArrayList();
        String requete = "SELECT * FROM `vehicule`";
        try {
            ste = (Statement) cnx.createStatement();
            ResultSet rs = ste.executeQuery(requete);

            while (rs.next()) {
                evs.add(new vehicule(rs.getInt("id"), rs.getString("name"), rs.getString("image"), rs.getString("contenu"), rs.getDate("updated"), rs.getDate("created"), rs.getInt("prix"), rs.getInt("total_en_stock"), rs.getInt("categories_id")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(vehiculeService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return evs;
    }

    public String GenerateQrev(vehicule ev) throws FileNotFoundException, IOException {
        String evName = "vehicule name: " + ev.getName() + "\n" + "vehicule date: " + ev.getUpdated() + "\n" + "vehicule created: " + ev.getCreated() + "\n" + "vehicule contenu: " + ev.getContenu() + "\n";
        ByteArrayOutputStream out = QRCode.from(evName).to(ImageType.JPG).stream();
        String filename = ev.getName() + "_QrCode.jpg";
        //File f = new File("src\\utils\\img\\" + filename);
                File f = new File("C:\\xampp\\htdocs\\xchangex\\imgQr\\qrcode" + filename);
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(out.toByteArray());
        fos.flush();

        System.out.println("qr yemshi");
        return filename;
    }


    public ObservableList<vehicule> chercherev(String chaine) {
        String sql = "SELECT * FROM vehicule WHERE (name LIKE ? or contenu LIKE ?  ) order by name ";
        //Connection cnx= Maconnexion.getInstance().getCnx();
        String ch = "%" + chaine + "%";
        ObservableList<vehicule> myList = FXCollections.observableArrayList();
        try {

            Statement ste = cnx.createStatement();
            // PreparedStatement pst = myCNX.getCnx().prepareStatement(requete6);
            PreparedStatement stee = cnx.prepareStatement(sql);
            stee.setString(1, ch);
            stee.setString(2, ch);

            ResultSet rs = stee.executeQuery();
            while (rs.next()) {
                vehicule e = new vehicule();

                e.setName(rs.getString("name"));

                e.setImage(rs.getString("Image"));
                e.setContenu(rs.getString("contenu"));
                e.setUpdated(rs.getDate("updated"));
                e.setCreated(rs.getDate("created"));
                e.setPrix(rs.getInt("prix"));
                e.setTotal_en_stock(rs.getInt("total_en_stock"));


                e.setId(rs.getInt("id"));

                myList.add(e);
                System.out.println("ev trouvé! ");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return myList;
    }

    public List<vehicule> trierev(String colonne, String ordre) throws SQLException {
        List<vehicule> vehicules = new ArrayList<>();

        // Vérification stricte pour éviter l'injection SQL
        List<String> colonnesValides = Arrays.asList("name", "prix");
        if (!colonnesValides.contains(colonne)) {
            colonne = "name"; // Valeur par défaut
        }
        if (!ordre.equalsIgnoreCase("ASC") && !ordre.equalsIgnoreCase("DESC")) {
            ordre = "ASC"; // Valeur par défaut
        }

        // Construction sécurisée de la requête SQL
        String sql = "SELECT * FROM vehicule ORDER BY " + colonne + " " + ordre;

        System.out.println("Requête SQL exécutée : " + sql); // Debug

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                vehicule e = new vehicule();
                e.setName(rs.getString("name"));
                e.setImage(rs.getString("Image"));
                e.setContenu(rs.getString("contenu"));
                e.setUpdated(rs.getDate("updated"));
                e.setCreated(rs.getDate("created"));
                e.setPrix(rs.getInt("prix"));
                e.setTotal_en_stock(rs.getInt("total_en_stock"));
                e.setId(rs.getInt("id"));
                vehicules.add(e);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors du tri des véhicules : " + ex.getMessage());
        }

        return vehicules;
    }


}
