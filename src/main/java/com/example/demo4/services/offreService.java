/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temvehiculee file, choose Tools | Temvehiculees
 * and open the temvehiculee in the editor.
 */
package com.example.demo4.services;

import com.example.demo4.entities.offre;
import com.example.demo4.entities.vehicule;
import com.example.demo4.entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.demo4.utils.MyDB;

/**
 *
 * @author asus
 */
public class offreService {

    Connection cnx;
    public Statement ste;
    public PreparedStatement pst;

    public offreService() {

        cnx = MyDB.getInstance().getCnx();
    }

    public void ajouteroffre(offre p) {
        User U = new User();
        vehiculeService es = new vehiculeService();
        String requete = "INSERT INTO `offre` (`created`,`vehicules_id` ,`id_user`,`rating`) VALUES(?,?,?,?) ;";

        try {
            vehicule tempev = es.FetchOneev(p.getVehicules_id());
            System.out.println("before" + tempev);
            tempev.setTotal_en_stock(Math.max(tempev.getTotal_en_stock() - 1, 0));
            es.modifiervehicule(tempev);
            int new_id = tempev.getId();
            p.setVehicule(tempev);
            System.out.println("after" + tempev);

            pst = (PreparedStatement) cnx.prepareStatement(requete);
            pst.setDate(1, p.getCreated());
            pst.setInt(2, p.getVehicules_id());
            pst.setInt(3, p.getId_user());
            pst.setDouble(4, p.getRating());

            pst.executeUpdate();


            System.out.println("offre with id ev = " + p.getVehicules_id() + " is added successfully");

        } catch (SQLException ex) {
            System.out.println("error in adding offre");
            Logger.getLogger(offreService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<offre> recupererOffreParDate(Date date) throws SQLException {
        List<offre> offres = new ArrayList<>();
        String query = "SELECT * FROM offre WHERE created = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setDate(1, date);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                offre o = new offre();
                o.setId(rs.getInt("id"));
                o.setContenu(rs.getString("contenu"));
                o.setCreated(rs.getDate("created"));
                o.setVehicules_id(rs.getInt("vehicules_id"));
                offres.add(o);
            }
        }
        return offres;
    }

    public List<offre> recupererOffre() throws SQLException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temvehiculees.
        offre dernierCommentaire = null;
        List<offre> particip = new ArrayList<>();
        String s = "SELECT * FROM offre WHERE id = (SELECT MAX(id) FROM offre)";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(s);
        while (rs.next()) {
            offre pa = new offre();
            pa.setId(rs.getInt("id"));
            pa.setId_user(rs.getInt("id_user"));
            pa.setVehicules_id(rs.getInt("vehicules_id"));
            pa.setCreated(rs.getDate("created"));
            pa.setContenu(rs.getString("contenu"));

            particip.add(pa);

        }
        return particip;
    }
    public List<offre> recupererComment() throws SQLException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temvehiculees.
        offre dernierCommentaire = null;
        List<offre> particip = new ArrayList<>();
        String s = "select * from offre";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(s);
        while (rs.next()) {
            offre pa = new offre();
            pa.setId(rs.getInt("id"));
            pa.setId_user(rs.getInt("id_user"));
            pa.setVehicules_id(rs.getInt("vehicules_id"));
            pa.setCreated(rs.getDate("created"));
            pa.setContenu(rs.getString("contenu"));

            particip.add(pa);

        }
        return particip;
    }

    public void supprimeroffre(offre p) throws SQLException {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temvehiculees.
        String req = "delete from offre where id  = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, p.getId());
        ps.executeUpdate();
        System.out.println("offre with id= " + p.getId() + "  is deleted successfully");
    }

    public offre FetchOneRes(int id) throws SQLException {
        offre r = new offre();
        String requete = "SELECT * FROM `offre` where id=" + id;

        try {
            ste = (Statement) cnx.createStatement();
            ResultSet rs = ste.executeQuery(requete);

            while (rs.next()) {

                r = new offre(rs.getInt("id"), rs.getDate("created"), rs.getInt("id_user"), rs.getInt("vehicules_id"), rs.getString("contenu"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(vehiculeService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }

    public void Deleteoffre(offre p) throws SQLException {
        vehiculeService es = new vehiculeService();
        offreService rs = new offreService();

        offre r = rs.FetchOneRes(p.getId());

        String requete = "delete from offre where id=" + p.getId();
        try {
            vehicule tempev = es.FetchOneev(r.getVehicules_id());
            System.out.println("before" + tempev);

            es.modifiervehicule(tempev);
            System.out.println("after" + tempev);
            pst = (PreparedStatement) cnx.prepareStatement(requete);
            //pst.setInt(1, id);

            pst.executeUpdate();
            System.out.println("offre with id=" + p.getId() + " is deleted successfully");
        } catch (SQLException ex) {
            System.out.println("error in delete offre " + ex.getMessage());
        }
    }

    public void modifieroffre(offre p) throws SQLException {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temvehiculees.
        String req = "UPDATE offre SET id_user = ?,vehicules_id = ?,created=?,contenu = ? where id = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, p.getId_user());
        ps.setInt(2, p.getVehicules_id());
        ps.setDate(3, p.getCreated());
        ps.setString(4, p.getContenu());
        ps.setInt(5, p.getId());


        ps.executeUpdate();
    }
    public void ajouterreserv(offre p) throws SQLException {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temvehiculees.
        String req = "UPDATE offre SET id_user = ?,vehicules_id = ?,created=?,contenu = ? where id = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, p.getId_user());
        ps.setInt(2, p.getVehicules_id());
        ps.setDate(3, p.getCreated());
        ps.setString(4, p.getContenu());
        ps.setInt(5, p.getId());


        ps.executeUpdate();
    }



    public double calculerTotalPrixOffre() throws SQLException {
        double total = 0;
        String requete = "SELECT p.prix FROM vehicule p JOIN offre pa ON p.id = pa.vehicules_id";

        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(requete);

        while (rs.next()) {
            total += rs.getDouble("prix");
        }

        return total;
    }

}
